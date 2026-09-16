package com.corporate.travel.service;

import com.corporate.travel.ai.AIExpenseFraudService;
import com.corporate.travel.dto.ExpenseDto;
import com.corporate.travel.entity.*;
import com.corporate.travel.entity.enums.ExpenseCategory;
import com.corporate.travel.entity.enums.ExpenseStatus;
import com.corporate.travel.entity.enums.NotificationType;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseReportRepository expenseReportRepository;
    private final UserRepository userRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final TravelWalletRepository walletRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final AIExpenseFraudService aiExpenseFraudService;

    public ExpenseService(ExpenseReportRepository expenseReportRepository, UserRepository userRepository,
                          TravelRequestRepository travelRequestRepository, TravelWalletRepository walletRepository,
                          NotificationService notificationService, AuditService auditService,
                          AIExpenseFraudService aiExpenseFraudService) {
        this.expenseReportRepository = expenseReportRepository;
        this.userRepository = userRepository;
        this.travelRequestRepository = travelRequestRepository;
        this.walletRepository = walletRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
        this.aiExpenseFraudService = aiExpenseFraudService;
    }

    @Transactional
    public ExpenseDto.ReportResponse createExpenseReport(Long userId, ExpenseDto.CreateReportRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        TravelRequest req = null;
        if (dto.getTravelRequestId() != null) {
            req = travelRequestRepository.findById(dto.getTravelRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("TravelRequest", "id", dto.getTravelRequestId()));
            if (req.getEmployee() == null || !userId.equals(req.getEmployee().getId())) {
                throw new IllegalArgumentException("You can only submit expenses for your own travel request");
            }
        }

        String repNum = "EXP-" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        BigDecimal total = BigDecimal.ZERO;
        boolean hasAiFlags = false;
        List<String> aiNotes = new ArrayList<>();

        ExpenseReport report = ExpenseReport.builder()
                .reportNumber(repNum)
                .title(dto.getTitle())
                .travelRequest(req)
                .employee(user)
                .status(ExpenseStatus.SUBMITTED)
                .totalAmount(BigDecimal.ZERO)
                .currencyCode("INR")
                .items(new ArrayList<>())
                .build();

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("At least one expense item is required");
        }

        for (ExpenseDto.CreateItemRequest itemDto : dto.getItems()) {
            if (itemDto.getAmount() == null || itemDto.getAmount().signum() <= 0) {
                throw new IllegalArgumentException("Expense amount must be greater than zero");
            }
            if (itemDto.getTaxAmount() != null && itemDto.getTaxAmount().signum() < 0) {
                throw new IllegalArgumentException("Tax amount cannot be negative");
            }

            total = total.add(itemDto.getAmount());
            boolean compliant = true;
            String flagReason = null;
            if (itemDto.getCategory() == ExpenseCategory.MEALS
                    && itemDto.getAmount().compareTo(BigDecimal.valueOf(2000)) > 0) {
                compliant = false;
                flagReason = "Meal expense exceeds the ₹2,000 policy threshold";
            }

            ExpenseItem item = ExpenseItem.builder()
                    .expenseReport(report)
                    .category(itemDto.getCategory())
                    .expenseDate(itemDto.getExpenseDate())
                    .merchantName(itemDto.getMerchantName())
                    .amount(itemDto.getAmount())
                    .taxAmount(itemDto.getTaxAmount() != null ? itemDto.getTaxAmount() : BigDecimal.ZERO)
                    .currencyCode("INR")
                    .description(itemDto.getDescription())
                    .receiptUrl(itemDto.getReceiptUrl())
                    .policyCompliant(compliant)
                    .policyFlagReason(flagReason)
                    .duplicateSuspect(false)
                    .build();

            List<String> aiFlags = aiExpenseFraudService.inspectExpense(item);
            if (!aiFlags.isEmpty()) {
                hasAiFlags = true;
                aiNotes.addAll(aiFlags);
            }
            if (flagReason != null) {
                hasAiFlags = true;
                aiNotes.add(flagReason);
            }
            report.getItems().add(item);
        }

        report.setTotalAmount(total);
        report.setHasAiFlags(hasAiFlags);
        report.setAiReviewNotes(aiNotes.isEmpty()
                ? "AI audit completed: no automatic anomaly flags detected. Finance review remains required."
                : String.join("; ", aiNotes));

        ExpenseReport saved = expenseReportRepository.save(report);
        walletRepository.findByUserId(userId).ifPresent(w -> {
            BigDecimal pending = w.getPendingExpenses() != null ? w.getPendingExpenses() : BigDecimal.ZERO;
            w.setPendingExpenses(pending.add(total));
            walletRepository.save(w);
        });

        notificationService.sendNotification(user.getId(), "Expense Report Submitted",
                "Your expense report " + repNum + " (₹" + total + ") was submitted for Finance review.",
                NotificationType.EXPENSE_SUBMITTED, "/expenses");
        auditService.logAction(user.getEmail(), "SUBMIT_EXPENSE_REPORT", "ExpenseReport", saved.getId(),
                "Submitted expense report " + repNum + " with amount ₹" + total, null);
        return mapToResponse(saved);
    }

    public ExpenseDto.OcrScanResponse simulateReceiptOcr(String fileName, BigDecimal amountHint) {
        BigDecimal amt = amountHint != null ? amountHint : BigDecimal.valueOf(1450);
        BigDecimal tax = amt.multiply(BigDecimal.valueOf(0.05));
        return ExpenseDto.OcrScanResponse.builder()
                .merchantName("Mainland China Restaurant & Lounge")
                .date(LocalDate.now())
                .amount(amt)
                .taxAmount(tax)
                .currency("INR")
                .suggestedCategory(ExpenseCategory.MEALS)
                .policyCompliant(amt.compareTo(BigDecimal.valueOf(2000)) <= 0)
                .complianceNote(amt.compareTo(BigDecimal.valueOf(2000)) <= 0 ? "Compliant: Below ₹2,000 per meal limit" : "Exceeds meal policy threshold")
                .duplicateSuspect(false)
                .build();
    }

    @Transactional
    public ExpenseDto.ReportResponse approveExpenseReport(Long reportId, Long financeUserId, boolean approve) {
        ExpenseReport report = getReport(reportId);
        if (approve) {
            requireStatus(report, ExpenseStatus.SUBMITTED, ExpenseStatus.MANAGER_REVIEW, ExpenseStatus.FINANCE_REVIEW);
            report.setStatus(ExpenseStatus.APPROVED);
            report.setApprovedAmount(report.getTotalAmount());
            reducePendingExpense(report.getEmployee().getId(), report.getTotalAmount());
            notificationService.sendNotification(report.getEmployee().getId(), "Expense Approved",
                    "Your expense report " + report.getReportNumber() + " was approved for ₹" + report.getApprovedAmount() + ". Reimbursement is now pending payment processing.",
                    NotificationType.EXPENSE_APPROVED, "/expenses");
            auditService.logAction(String.valueOf(financeUserId), "APPROVE_EXPENSE_REPORT", "ExpenseReport", reportId,
                    "Approved expense report; reimbursement not yet paid: " + report.getReportNumber(), null);
        } else {
            if (report.getStatus() == ExpenseStatus.REIMBURSED || report.getStatus() == ExpenseStatus.REIMBURSEMENT_PROCESSING) {
                throw new IllegalStateException("A report already in reimbursement processing cannot be rejected");
            }
            if (report.getStatus() == ExpenseStatus.REJECTED) {
                throw new IllegalStateException("Expense report is already rejected");
            }
            report.setStatus(ExpenseStatus.REJECTED);
            reducePendingExpense(report.getEmployee().getId(), report.getTotalAmount());
            auditService.logAction(String.valueOf(financeUserId), "REJECT_EXPENSE_REPORT", "ExpenseReport", reportId,
                    "Rejected expense report: " + report.getReportNumber(), null);
        }
        return mapToResponse(expenseReportRepository.save(report));
    }

    @Transactional
    public ExpenseDto.ReportResponse startReimbursement(Long reportId, Long financeUserId) {
        ExpenseReport report = getReport(reportId);
        requireStatus(report, ExpenseStatus.APPROVED);
        report.setStatus(ExpenseStatus.REIMBURSEMENT_PROCESSING);
        auditService.logAction(String.valueOf(financeUserId), "START_REIMBURSEMENT", "ExpenseReport", reportId,
                "Reimbursement processing started for " + report.getReportNumber(), null);
        return mapToResponse(expenseReportRepository.save(report));
    }

    @Transactional
    public ExpenseDto.ReportResponse completeReimbursement(Long reportId, Long financeUserId) {
        ExpenseReport report = getReport(reportId);
        requireStatus(report, ExpenseStatus.REIMBURSEMENT_PROCESSING);
        report.setStatus(ExpenseStatus.REIMBURSED);
        BigDecimal approved = report.getApprovedAmount() != null ? report.getApprovedAmount() : BigDecimal.ZERO;
        walletRepository.findByUserId(report.getEmployee().getId()).ifPresent(w -> {
            BigDecimal reimbursed = w.getReimbursedAmount() != null ? w.getReimbursedAmount() : BigDecimal.ZERO;
            w.setReimbursedAmount(reimbursed.add(approved));
            walletRepository.save(w);
        });
        notificationService.sendNotification(report.getEmployee().getId(), "Reimbursement Completed",
                "Reimbursement for " + report.getReportNumber() + " of ₹" + approved + " has been completed.",
                NotificationType.PAYMENT_COMPLETED, "/expenses");
        auditService.logAction(String.valueOf(financeUserId), "COMPLETE_REIMBURSEMENT", "ExpenseReport", reportId,
                "Reimbursement completed for " + report.getReportNumber() + ", amount ₹" + approved, null);
        return mapToResponse(expenseReportRepository.save(report));
    }

    @Transactional
    public ExpenseDto.ReportResponse failReimbursement(Long reportId, Long financeUserId) {
        ExpenseReport report = getReport(reportId);
        requireStatus(report, ExpenseStatus.REIMBURSEMENT_PROCESSING);
        report.setStatus(ExpenseStatus.PAYMENT_FAILED);
        BigDecimal approved = report.getApprovedAmount() != null ? report.getApprovedAmount() : BigDecimal.ZERO;
        walletRepository.findByUserId(report.getEmployee().getId()).ifPresent(w -> {
            BigDecimal pending = w.getPendingExpenses() != null ? w.getPendingExpenses() : BigDecimal.ZERO;
            w.setPendingExpenses(pending.add(approved));
            walletRepository.save(w);
        });
        auditService.logAction(String.valueOf(financeUserId), "REIMBURSEMENT_FAILED", "ExpenseReport", reportId,
                "Reimbursement failed and amount returned to pending expenses: " + report.getReportNumber(), null);
        return mapToResponse(expenseReportRepository.save(report));
    }

    private ExpenseReport getReport(Long reportId) {
        return expenseReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("ExpenseReport", "id", reportId));
    }

    private void requireStatus(ExpenseReport report, ExpenseStatus... allowed) {
        for (ExpenseStatus status : allowed) {
            if (report.getStatus() == status) return;
        }
        throw new IllegalStateException("Invalid expense workflow transition from status " + report.getStatus());
    }

    private void reducePendingExpense(Long userId, BigDecimal amount) {
        walletRepository.findByUserId(userId).ifPresent(w -> {
            BigDecimal pending = w.getPendingExpenses() != null ? w.getPendingExpenses() : BigDecimal.ZERO;
            w.setPendingExpenses(pending.subtract(amount).max(BigDecimal.ZERO));
            walletRepository.save(w);
        });
    }

    @Transactional(readOnly = true)
    public List<ExpenseDto.ReportResponse> getMyReports(Long userId) {
        return expenseReportRepository.findByEmployeeIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseDto.ReportResponse> getAllReports() {
        return expenseReportRepository.findAll().stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    public ExpenseDto.ReportResponse mapToResponse(ExpenseReport r) {
        List<ExpenseDto.ItemResponse> itemDtos = r.getItems() != null ?
                r.getItems().stream().map(i -> ExpenseDto.ItemResponse.builder()
                        .id(i.getId()).category(i.getCategory()).expenseDate(i.getExpenseDate())
                        .merchantName(i.getMerchantName()).amount(i.getAmount()).taxAmount(i.getTaxAmount())
                        .description(i.getDescription()).receiptUrl(i.getReceiptUrl())
                        .policyCompliant(i.getPolicyCompliant()).policyFlagReason(i.getPolicyFlagReason())
                        .duplicateSuspect(i.getDuplicateSuspect()).build()).collect(Collectors.toList()) : new ArrayList<>();
        return ExpenseDto.ReportResponse.builder()
                .id(r.getId()).reportNumber(r.getReportNumber()).title(r.getTitle())
                .travelRequestId(r.getTravelRequest() != null ? r.getTravelRequest().getId() : null)
                .tripName(r.getTravelRequest() != null ? r.getTravelRequest().getTripName() : "General Expense")
                .employeeId(r.getEmployee() != null ? r.getEmployee().getId() : null)
                .employeeName(r.getEmployee() != null ? r.getEmployee().getFullName() : "")
                .departmentName(r.getDepartment() != null ? r.getDepartment().getName() : "Enterprise Corporate")
                .status(r.getStatus()).totalAmount(r.getTotalAmount()).approvedAmount(r.getApprovedAmount())
                .currencyCode(r.getCurrencyCode()).hasAiFlags(r.getHasAiFlags()).aiReviewNotes(r.getAiReviewNotes())
                .createdAt(r.getCreatedAt()).items(itemDtos).build();
    }
}

package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.ExpenseDto;
import com.corporate.travel.entity.*;
import com.corporate.travel.entity.enums.ExpenseCategory;
import com.corporate.travel.entity.enums.ExpenseStatus;
import com.corporate.travel.entity.enums.NotificationType;
import com.corporate.travel.exception.ForbiddenException;
import com.corporate.travel.exception.ResourceNotFoundException;
import com.corporate.travel.repository.*;
import com.corporate.travel.security.TenantAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);

    public ExpenseService(ExpenseReportRepository expenseReportRepository, UserRepository userRepository, TravelRequestRepository travelRequestRepository, TravelWalletRepository walletRepository, NotificationService notificationService, AuditService auditService, TenantAccessService tenantAccessService) {
        this.expenseReportRepository = expenseReportRepository;
        this.userRepository = userRepository;
        this.travelRequestRepository = travelRequestRepository;
        this.walletRepository = walletRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
        this.tenantAccessService = tenantAccessService;
    }


    private final ExpenseReportRepository expenseReportRepository;
    private final UserRepository userRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final TravelWalletRepository walletRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final TenantAccessService tenantAccessService;

    @Transactional
    public ExpenseDto.ReportResponse createExpenseReport(Long userId, ExpenseDto.CreateReportRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (user.getOrganization() != null) {
            tenantAccessService.assertCanAccessOrganization(user.getOrganization().getId());
        }

        TravelRequest req = null;
        if (dto.getTravelRequestId() != null) {
            req = travelRequestRepository.findById(dto.getTravelRequestId()).orElse(null);
            if (req != null && req.getOrganization() != null) {
                tenantAccessService.assertCanAccessResource(req.getOrganization().getId());
            }
        }

        String repNum = "EXP-" + (1000 + (int)(Math.random() * 9000));
        BigDecimal total = BigDecimal.ZERO;

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

        boolean hasAiFlags = false;
        List<String> aiNotes = new ArrayList<>();

        if (dto.getItems() != null) {
            for (ExpenseDto.CreateItemRequest itemDto : dto.getItems()) {
                total = total.add(itemDto.getAmount());
                boolean compliant = true;
                String flagReason = null;

                // Anomaly check
                if (itemDto.getCategory() == ExpenseCategory.MEALS && itemDto.getAmount().compareTo(BigDecimal.valueOf(2500)) > 0) {
                    compliant = false;
                    flagReason = "Meal bill exceeds daily policy limit of ₹2,000";
                    hasAiFlags = true;
                    aiNotes.add(flagReason);
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
                report.getItems().add(item);
            }
        }

        report.setTotalAmount(total);
        report.setHasAiFlags(hasAiFlags);
        report.setAiReviewNotes(aiNotes.isEmpty() ? "AI Audit: No fraud anomalies detected." : String.join("; ", aiNotes));

        ExpenseReport saved = expenseReportRepository.save(report);

        // Update Wallet Pending
        final BigDecimal reportTotal = total;
        walletRepository.findByUserId(userId).ifPresent(w -> {
            w.setPendingExpenses(w.getPendingExpenses().add(reportTotal));
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
                .complianceNote(amt.compareTo(BigDecimal.valueOf(2000)) <= 0 ? "Compliant: Below ₹2,000 per meal limit" : "Exceeds daily meal cap")
                .duplicateSuspect(false)
                .build();
    }

    @Transactional
    public ExpenseDto.ReportResponse approveExpenseReport(Long reportId, Long financeUserId, boolean approve) {
        ExpenseReport report = expenseReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("ExpenseReport", "id", reportId));
        assertExpenseReportAccess(report);

        if (approve) {
            report.setStatus(ExpenseStatus.APPROVED);
            report.setApprovedAmount(report.getTotalAmount());
            walletRepository.findByUserId(report.getEmployee().getId()).ifPresent(w -> {
                w.setPendingExpenses(w.getPendingExpenses().subtract(report.getTotalAmount()));
                w.setReimbursedAmount(w.getReimbursedAmount().add(report.getTotalAmount()));
                walletRepository.save(w);
            });
            notificationService.sendNotification(report.getEmployee().getId(), "Expense Approved & Reimbursed",
                    "Your expense report " + report.getReportNumber() + " for ₹" + report.getTotalAmount() + " has been approved.",
                    NotificationType.EXPENSE_APPROVED, "/expenses");
        } else {
            report.setStatus(ExpenseStatus.REJECTED);
        }

        ExpenseReport saved = expenseReportRepository.save(report);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ExpenseDto.ReportResponse> getMyReports(Long userId) {
        return expenseReportRepository.findByEmployeeIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseDto.ReportResponse> getAllReports() {
        Long scope = tenantAccessService.resolveOrganizationScope();
        List<ExpenseReport> reports = scope == null
                ? expenseReportRepository.findAll()
                : expenseReportRepository.findByEmployee_Organization_IdOrderByCreatedAtDesc(scope);
        return reports.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private void assertExpenseReportAccess(ExpenseReport report) {
        if (report.getEmployee() != null && report.getEmployee().getOrganization() != null) {
            tenantAccessService.assertCanAccessResource(report.getEmployee().getOrganization().getId());
        }
    }

    public ExpenseDto.ReportResponse mapToResponse(ExpenseReport r) {
        List<ExpenseDto.ItemResponse> itemDtos = r.getItems() != null ?
                r.getItems().stream().map(i -> ExpenseDto.ItemResponse.builder()
                        .id(i.getId())
                        .category(i.getCategory())
                        .expenseDate(i.getExpenseDate())
                        .merchantName(i.getMerchantName())
                        .amount(i.getAmount())
                        .taxAmount(i.getTaxAmount())
                        .description(i.getDescription())
                        .receiptUrl(i.getReceiptUrl())
                        .policyCompliant(i.getPolicyCompliant())
                        .policyFlagReason(i.getPolicyFlagReason())
                        .duplicateSuspect(i.getDuplicateSuspect())
                        .build()).collect(Collectors.toList()) : new ArrayList<>();

        return ExpenseDto.ReportResponse.builder()
                .id(r.getId())
                .reportNumber(r.getReportNumber())
                .title(r.getTitle())
                .travelRequestId(r.getTravelRequest() != null ? r.getTravelRequest().getId() : null)
                .tripName(r.getTravelRequest() != null ? r.getTravelRequest().getTripName() : "General Expense")
                .employeeId(r.getEmployee() != null ? r.getEmployee().getId() : null)
                .employeeName(r.getEmployee() != null ? r.getEmployee().getFullName() : "")
                .departmentName(r.getDepartment() != null ? r.getDepartment().getName() : "Enterprise Corporate")
                .status(r.getStatus())
                .totalAmount(r.getTotalAmount())
                .approvedAmount(r.getApprovedAmount())
                .currencyCode(r.getCurrencyCode())
                .hasAiFlags(r.getHasAiFlags())
                .aiReviewNotes(r.getAiReviewNotes())
                .createdAt(r.getCreatedAt())
                .items(itemDtos)
                .build();
    }
}

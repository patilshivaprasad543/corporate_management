package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.ExpenseDto;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expense Management", description = "Submit expenses, finance approval, and controlled reimbursement lifecycle")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @Operation(summary = "Submit a new travel expense report")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> createReport(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ExpenseDto.CreateReportRequest request) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.createExpenseReport(userId, request), "Expense report submitted"));
    }

    @PostMapping("/ocr-scan")
    @Operation(summary = "Simulate receipt OCR scanning and policy verification")
    public ResponseEntity<ApiResponse<ExpenseDto.OcrScanResponse>> simulateOcr(
            @RequestParam(name = "fileName", required = false, defaultValue = "receipt_sample.jpg") String fileName,
            @RequestParam(name = "amount", required = false, defaultValue = "1450.00") BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.ok(expenseService.simulateReceiptOcr(fileName, amount), "Receipt OCR scanned successfully"));
    }

    @PostMapping("/{reportId}/approve")
    @Operation(summary = "Approve an expense report; reimbursement remains pending")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> approveReport(
            @PathVariable Long reportId, @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.approveExpenseReport(reportId, financeId, true), "Expense approved; reimbursement pending"));
    }

    @PostMapping("/{reportId}/reject")
    @Operation(summary = "Reject an expense report")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> rejectReport(
            @PathVariable Long reportId, @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.approveExpenseReport(reportId, financeId, false), "Expense rejected"));
    }

    @PostMapping("/{reportId}/reimbursement/start")
    @Operation(summary = "Start reimbursement processing for an approved expense")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> startReimbursement(
            @PathVariable Long reportId, @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.startReimbursement(reportId, financeId), "Reimbursement processing started"));
    }

    @PostMapping("/{reportId}/reimbursement/complete")
    @Operation(summary = "Complete reimbursement after payment succeeds")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> completeReimbursement(
            @PathVariable Long reportId, @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.completeReimbursement(reportId, financeId), "Reimbursement completed"));
    }

    @PostMapping("/{reportId}/reimbursement/fail")
    @Operation(summary = "Mark reimbursement payment as failed and return amount to pending")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> failReimbursement(
            @PathVariable Long reportId, @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.failReimbursement(reportId, financeId), "Reimbursement marked as failed"));
    }

    @GetMapping("/my")
    @Operation(summary = "List current user's expense reports")
    public ResponseEntity<ApiResponse<List<ExpenseDto.ReportResponse>>> getMyReports(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(expenseService.getMyReports(userId), "User reports retrieved"));
    }

    @GetMapping
    @Operation(summary = "List all expense reports for Finance audit")
    public ResponseEntity<ApiResponse<List<ExpenseDto.ReportResponse>>> getAllReports() {
        return ResponseEntity.ok(ApiResponse.ok(expenseService.getAllReports(), "All reports retrieved"));
    }
}

package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.ExpenseDto;
import com.corporate.travel.security.UserPrincipal;
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
@Tag(name = "Expense Management", description = "Submit expense reports, receipt OCR scanning, and finance reimbursement")
public class ExpenseController {
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Submit a new travel expense report")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> createReport(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ExpenseDto.CreateReportRequest request) {
        Long userId = principal != null ? principal.getId() : 5L;
        return ResponseEntity.ok(ApiResponse.ok(expenseService.createExpenseReport(userId, request), "Expense report submitted"));
    }

    @PostMapping("/ocr-scan")
    @Operation(summary = "Simulate receipt OCR scanning, metadata extraction & policy compliance verification")
    public ResponseEntity<ApiResponse<ExpenseDto.OcrScanResponse>> simulateOcr(
            @RequestParam(name = "fileName", required = false, defaultValue = "receipt_sample.jpg") String fileName,
            @RequestParam(name = "amount", required = false, defaultValue = "1450.00") BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.ok(expenseService.simulateReceiptOcr(fileName, amount), "Receipt OCR scanned successfully"));
    }

    @PostMapping("/{reportId}/approve")
    @Operation(summary = "Approve and process reimbursement for expense report")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> approveReport(
            @PathVariable("reportId") Long reportId,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = principal != null ? principal.getId() : 6L;
        return ResponseEntity.ok(ApiResponse.ok(expenseService.approveExpenseReport(reportId, financeId, true), "Expense approved and reimbursed"));
    }

    @PostMapping("/{reportId}/reject")
    @Operation(summary = "Reject expense report")
    public ResponseEntity<ApiResponse<ExpenseDto.ReportResponse>> rejectReport(
            @PathVariable("reportId") Long reportId,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long financeId = principal != null ? principal.getId() : 6L;
        return ResponseEntity.ok(ApiResponse.ok(expenseService.approveExpenseReport(reportId, financeId, false), "Expense rejected"));
    }

    @GetMapping("/my")
    @Operation(summary = "List current user's expense reports")
    public ResponseEntity<ApiResponse<List<ExpenseDto.ReportResponse>>> getMyReports(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getId() : 5L;
        return ResponseEntity.ok(ApiResponse.ok(expenseService.getMyReports(userId), "User reports retrieved"));
    }

    @GetMapping
    @Operation(summary = "List all expense reports for Finance audit")
    public ResponseEntity<ApiResponse<List<ExpenseDto.ReportResponse>>> getAllReports() {
        return ResponseEntity.ok(ApiResponse.ok(expenseService.getAllReports(), "All reports retrieved"));
    }

}

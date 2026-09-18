package com.corporate.travel.controller;

import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.service.PdfDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "PDF itineraries, boarding passes, and expense reports")
public class DocumentController {

    private final PdfDocumentService pdfDocumentService;

    public DocumentController(PdfDocumentService pdfDocumentService) {
        this.pdfDocumentService = pdfDocumentService;
    }

    @GetMapping("/bookings/{id}/boarding-pass.pdf")
    @Operation(summary = "Download boarding pass PDF")
    public ResponseEntity<byte[]> boardingPass(@PathVariable("id") Long id,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        byte[] pdf = pdfDocumentService.generateBoardingPassPdf(id, userId);
        return pdfResponse(pdf, "boarding-pass-" + id + ".pdf");
    }

    @GetMapping("/bookings/{id}/itinerary.pdf")
    @Operation(summary = "Download itinerary PDF")
    public ResponseEntity<byte[]> itinerary(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        byte[] pdf = pdfDocumentService.generateItineraryPdf(id, userId);
        return pdfResponse(pdf, "itinerary-" + id + ".pdf");
    }

    @GetMapping("/expenses/{id}/report.pdf")
    @Operation(summary = "Download expense report PDF")
    public ResponseEntity<byte[]> expenseReport(@PathVariable("id") Long id,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        byte[] pdf = pdfDocumentService.generateExpenseReportPdf(id, userId);
        return pdfResponse(pdf, "expense-report-" + id + ".pdf");
    }

    private ResponseEntity<byte[]> pdfResponse(byte[] pdf, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.BookingDto;
import com.corporate.travel.security.UserPrincipal;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Create bookings, issue PNRs/E-Tickets, manage itineraries, and handle corporate payments")
public class BookingController {
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_BOOKING_CREATE')")
    @Operation(summary = "Book flight/hotel/transport with PNR generation and itinerary sync")
    public ResponseEntity<ApiResponse<BookingDto.Response>> createBooking(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BookingDto.CreateBookingRequest request) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(bookingService.createBooking(userId, request), "Booking confirmed successfully"));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('PERM_BOOKING_VIEW')")
    @Operation(summary = "List all bookings for current user")
    public ResponseEntity<ApiResponse<List<BookingDto.Response>>> getMyBookings(
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getMyBookings(userId), "User bookings retrieved"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_BOOKING_VIEW')")
    @Operation(summary = "List all bookings in organization")
    public ResponseEntity<ApiResponse<List<BookingDto.Response>>> getAllBookings() {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getAllBookings(), "All bookings retrieved"));
    }

}

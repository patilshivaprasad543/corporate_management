package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.BookingDto;
import com.corporate.travel.entity.*;
import com.corporate.travel.entity.enums.*;
import com.corporate.travel.exception.BadRequestException;
import com.corporate.travel.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import com.corporate.travel.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final TravelRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final ItineraryRepository itineraryRepository;
    private final TravelWalletRepository walletRepository;
    private final PaymentTransactionRepository paymentRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public BookingService(BookingRepository bookingRepository, TravelRequestRepository requestRepository, UserRepository userRepository,
                          OrganizationRepository organizationRepository, ItineraryRepository itineraryRepository,
                          TravelWalletRepository walletRepository, PaymentTransactionRepository paymentRepository,
                          NotificationService notificationService, AuditService auditService) {
        this.bookingRepository = bookingRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.itineraryRepository = itineraryRepository;
        this.walletRepository = walletRepository;
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Transactional
    public BookingDto.Response createBooking(Long userId, BookingDto.CreateBookingRequest dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (dto == null || dto.getBookingType() == null || dto.getPaymentMethod() == null) {
            throw new BadRequestException("Booking type and payment method are required");
        }
        if (dto.getHotelNights() != null && dto.getHotelNights() <= 0) {
            throw new BadRequestException("Hotel nights must be greater than zero");
        }

        TravelRequest request = null;
        if (dto.getTravelRequestId() != null) {
            request = requestRepository.findById(dto.getTravelRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("TravelRequest", "id", dto.getTravelRequestId()));
            if (request.getEmployee() == null || request.getEmployee().getId() == null) {
                throw new BadRequestException("Travel request has no valid employee");
            }
            boolean owner = request.getEmployee().getId().equals(userId);
            boolean bookingManager = user.getRoles() != null && user.getRoles().stream().anyMatch(role -> {
                String name = role.getName().name();
                return "ROLE_TRAVEL_MANAGER".equals(name) || "ROLE_COMPANY_ADMIN".equals(name) || "ROLE_SUPER_ADMIN".equals(name);
            });
            if (!owner && !bookingManager) {
                throw new AccessDeniedException("You can only book your own travel requests unless you are an authorized travel manager");
            }
            if (request.getStatus() != RequestStatus.APPROVED && request.getStatus() != RequestStatus.BOOKING_IN_PROGRESS) {
                throw new BadRequestException("Travel request must be fully approved before booking");
            }
            boolean activeBookingExists = bookingRepository.findByTravelRequestId(request.getId()).stream().anyMatch(b ->
                    b.getStatus() != BookingStatus.CANCELLED && b.getStatus() != BookingStatus.REFUNDED);
            if (activeBookingExists) {
                throw new BadRequestException("An active booking already exists for this travel request");
            }
            request.setStatus(RequestStatus.BOOKING_IN_PROGRESS);
            requestRepository.save(request);
        }

        Organization org = user.getOrganization() != null ? user.getOrganization() : organizationRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new BadRequestException("No organization is available for this booking"));

        String pnr = "PNR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        String ref = "BK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        String eTicket = "ETK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();

        BigDecimal basePrice = BigDecimal.valueOf(5400);
        if (dto.getBookingType() == BookingType.HOTEL) {
            int nights = dto.getHotelNights() != null ? dto.getHotelNights() : 2;
            basePrice = BigDecimal.valueOf(7500L * nights);
        } else if (dto.getBookingType() == BookingType.COMBO) {
            basePrice = BigDecimal.valueOf(20400);
        }
        BigDecimal tax = basePrice.multiply(BigDecimal.valueOf(0.12));
        BigDecimal total = basePrice.add(tax);

        if (!Boolean.TRUE.equals(dto.getPersonalBooking())) {
            walletRepository.findByUserId(userId).ifPresent(wallet -> {
                if (wallet.getRemainingBudget().compareTo(total) < 0) {
                    throw new BadRequestException("Insufficient corporate travel budget for this booking");
                }
            });
        }

        Booking booking = Booking.builder().bookingReference(ref).pnrNumber(pnr).bookingType(dto.getBookingType())
                .status(BookingStatus.CONFIRMED).travelRequest(request).user(user).organization(org).totalAmount(total)
                .taxAmount(tax).currencyCode("INR").paymentMethod(dto.getPaymentMethod())
                .personalBooking(dto.getPersonalBooking() != null ? dto.getPersonalBooking() : false).eTicketNumber(eTicket).build();

        List<BookingItem> items = new ArrayList<>();
        items.add(BookingItem.builder().booking(booking).itemType(dto.getBookingType().name())
                .title(dto.getBookingType() == BookingType.FLIGHT ? "Air India AI-839 (HYD -> DEL)" : "Taj Palace Deluxe Suite")
                .description("Confirmed with corporate discount rate").supplierReference(pnr).price(basePrice).build());
        booking.setItems(items);
        Booking savedBooking = bookingRepository.save(booking);

        paymentRepository.save(PaymentTransaction.builder().transactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .booking(savedBooking).user(user).amount(total).currencyCode("INR").paymentMethod(dto.getPaymentMethod())
                .status(PaymentStatus.SUCCESSFUL).providerGateway("CORPORATE_VIRTUAL_GATEWAY").build());

        if (!Boolean.TRUE.equals(dto.getPersonalBooking())) {
            walletRepository.findByUserId(userId).ifPresent(wallet -> {
                wallet.setUsedBudget(wallet.getUsedBudget().add(total));
                walletRepository.save(wallet);
            });
        }
        if (request != null) {
            request.setStatus(RequestStatus.CONFIRMED);
            requestRepository.save(request);
        }
        buildItineraryForBooking(user, request, savedBooking);
        notificationService.sendNotification(user.getId(), "Booking Confirmed - " + pnr,
                "Your travel booking " + ref + " (PNR: " + pnr + ") is confirmed. E-ticket issued.", NotificationType.BOOKING_CONFIRMED, "/itinerary");
        auditService.logAction(user.getEmail(), "CREATE_BOOKING", "Booking", savedBooking.getId(),
                "Confirmed booking " + ref + " with total amount ₹" + total, null);
        return mapToResponse(savedBooking);
    }

    private void buildItineraryForBooking(User user, TravelRequest request, Booking booking) {
        String tripTitle = request != null ? request.getTripName() : "Business Trip to Delhi";
        LocalDate start = request != null ? request.getDepartureDate() : LocalDate.now().plusDays(5);
        LocalDate end = request != null && request.getReturnDate() != null ? request.getReturnDate() : start.plusDays(3);
        Itinerary itinerary = itineraryRepository.findByUserIdOrderByStartDateDesc(user.getId()).stream().findFirst()
                .orElseGet(() -> Itinerary.builder().title(tripTitle).travelRequest(request).user(user).startDate(start).endDate(end).events(new ArrayList<>()).build());
        List<ItineraryEvent> events = itinerary.getEvents() != null ? itinerary.getEvents() : new ArrayList<>();
        LocalDateTime dep = start.atTime(7, 30);
        events.add(ItineraryEvent.builder().itinerary(itinerary).eventTime(dep).title("Flight AI-839 (Hyderabad -> Delhi)")
                .location("Rajiv Gandhi Intl Airport Terminal 1").eventType("FLIGHT").notes("Check-in 2 hours prior. PNR: " + booking.getPnrNumber()).confirmationCode(booking.getPnrNumber()).build());
        events.add(ItineraryEvent.builder().itinerary(itinerary).eventTime(dep.plusHours(3)).title("Airport Executive Transfer to Hotel")
                .location("Delhi Airport Terminal 3 Uber Zone").eventType("TRANSFER").notes("Uber Premium sedan reserved").confirmationCode("UBER-TRIP-749").build());
        events.add(ItineraryEvent.builder().itinerary(itinerary).eventTime(dep.plusHours(4).plusMinutes(30)).title("Hotel Check-in: Taj Palace")
                .location("Diplomatic Enclave, Chanakyapuri").eventType("HOTEL_CHECKIN").notes("Early check-in requested. Corporate loyalty badge applied.").confirmationCode("HTL-TAJ-992").build());
        events.add(ItineraryEvent.builder().itinerary(itinerary).eventTime(dep.plusHours(7)).title("Client Business Review & Strategy Session")
                .location("Cyber City Tower B, 14th Floor").eventType("MEETING").notes("Present Q3 corporate tech roadmap").build());
        itinerary.setEvents(events);
        itineraryRepository.save(itinerary);
    }

    @Transactional(readOnly = true)
    public List<BookingDto.Response> getMyBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDto.Response> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public BookingDto.Response mapToResponse(Booking b) {
        List<BookingDto.BookingItemDto> itemDtos = b.getItems() != null ? b.getItems().stream().map(i -> BookingDto.BookingItemDto.builder()
                .id(i.getId()).itemType(i.getItemType()).title(i.getTitle()).description(i.getDescription())
                .supplierReference(i.getSupplierReference()).price(i.getPrice()).build()).collect(Collectors.toList()) : new ArrayList<>();
        return BookingDto.Response.builder().id(b.getId()).bookingReference(b.getBookingReference()).pnrNumber(b.getPnrNumber())
                .bookingType(b.getBookingType()).status(b.getStatus()).travelRequestId(b.getTravelRequest() != null ? b.getTravelRequest().getId() : null)
                .tripName(b.getTravelRequest() != null ? b.getTravelRequest().getTripName() : "Direct Booking").totalAmount(b.getTotalAmount())
                .taxAmount(b.getTaxAmount()).currencyCode(b.getCurrencyCode()).paymentMethod(b.getPaymentMethod())
                .personalBooking(b.getPersonalBooking()).eTicketNumber(b.getETicketNumber()).createdAt(b.getCreatedAt()).items(itemDtos).build();
    }
}

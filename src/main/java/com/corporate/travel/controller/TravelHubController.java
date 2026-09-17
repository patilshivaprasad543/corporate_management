package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.TravelHubDto;
import com.corporate.travel.entity.*;
import com.corporate.travel.repository.*;
import com.corporate.travel.security.AuthenticatedUser;
import com.corporate.travel.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/travel-hubs")
public class TravelHubController {
    private final TravelRequestRepository requestRepository;
    private final TravelBudgetRepository budgetRepository;
    private final TravelDocumentRepository documentRepository;
    private final TravelAgentContactRepository contactRepository;
    private final TravelChatMessageRepository chatRepository;
    private final UserRepository userRepository;

    public TravelHubController(TravelRequestRepository requestRepository, TravelBudgetRepository budgetRepository,
                               TravelDocumentRepository documentRepository, TravelAgentContactRepository contactRepository,
                               TravelChatMessageRepository chatRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository; this.budgetRepository = budgetRepository;
        this.documentRepository = documentRepository; this.contactRepository = contactRepository;
        this.chatRepository = chatRepository; this.userRepository = userRepository;
    }

    @GetMapping("/{travelRequestId}")
    public ResponseEntity<ApiResponse<TravelHubDto.TravelHubResponse>> getHub(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long travelRequestId) {
        Long userId = AuthenticatedUser.requireId(principal);
        return ResponseEntity.ok(ApiResponse.ok(toHub(loadAuthorizedRequest(travelRequestId, userId)), "Travel hub retrieved"));
    }

    @PostMapping("/{travelRequestId}/budget")
    public ResponseEntity<ApiResponse<TravelHubDto.BudgetResponse>> allocateBudget(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long travelRequestId, @Valid @RequestBody TravelHubDto.BudgetRequest body) {
        Long userId = AuthenticatedUser.requireId(principal); requireRole(principal, "ROLE_HR", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN");
        TravelRequest request = requestRepository.findById(travelRequestId).orElseThrow();
        TravelBudget budget = budgetRepository.findByTravelRequestId(travelRequestId).orElseGet(TravelBudget::new);
        budget.setTravelRequest(request); budget.setAllocatedAmount(body.allocatedAmount());
        budget.setFlightLimit(zero(body.flightLimit())); budget.setHotelLimit(zero(body.hotelLimit())); budget.setCabLimit(zero(body.cabLimit())); budget.setMealLimit(zero(body.mealLimit())); budget.setOtherLimit(zero(body.otherLimit()));
        budget.setAllocatedBy(userRepository.findById(userId).orElseThrow()); budget.setStatus(TravelBudget.BudgetStatus.ALLOCATED);
        return ResponseEntity.ok(ApiResponse.ok(toBudget(budgetRepository.save(budget)), "HR budget allocated"));
    }

    @PostMapping("/{travelRequestId}/agent-contact")
    public ResponseEntity<ApiResponse<TravelHubDto.AgentContactResponse>> saveAgentContact(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long travelRequestId, @Valid @RequestBody TravelHubDto.AgentContactRequest body) {
        requireRole(principal, "ROLE_VENDOR", "ROLE_HR", "ROLE_COMPANY_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_SUPPORT");
        TravelRequest request = requestRepository.findById(travelRequestId).orElseThrow();
        TravelAgentContact c = contactRepository.findByTravelRequestId(travelRequestId).orElseGet(TravelAgentContact::new);
        c.setTravelRequest(request); c.setAgencyName(body.agencyName()); c.setAgentName(body.agentName()); c.setPhoneNumber(body.phoneNumber()); c.setEmail(body.email()); c.setEmergencyPhone(body.emergencyPhone()); c.setNotes(body.notes());
        return ResponseEntity.ok(ApiResponse.ok(toContact(contactRepository.save(c)), "Travel agent contact saved"));
    }

    @PostMapping("/{travelRequestId}/chat")
    public ResponseEntity<ApiResponse<TravelHubDto.ChatMessageResponse>> sendChat(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long travelRequestId, @Valid @RequestBody TravelHubDto.ChatRequest body) {
        Long userId = AuthenticatedUser.requireId(principal); TravelRequest request = loadAuthorizedRequest(travelRequestId, userId);
        TravelChatMessage m = new TravelChatMessage(); m.setTravelRequest(request); m.setSender(userRepository.findById(userId).orElseThrow()); m.setMessage(body.message()); m.setAttachmentName(body.attachmentName()); m.setAttachmentKey(body.attachmentKey());
        return ResponseEntity.ok(ApiResponse.ok(toChat(chatRepository.save(m)), "Message sent"));
    }

    private TravelRequest loadAuthorizedRequest(Long id, Long userId) {
        TravelRequest r = requestRepository.findById(id).orElseThrow();
        boolean owner = r.getEmployee() != null && userId.equals(r.getEmployee().getId());
        boolean privileged = userRepository.findById(userId).map(u -> u.getRoles().stream().anyMatch(role -> List.of("ROLE_COMPANY_ADMIN","ROLE_SUPER_ADMIN","ROLE_HR","ROLE_FINANCE","ROLE_SUPPORT","ROLE_APPROVER","ROLE_TRAVEL_MANAGER","ROLE_VENDOR").contains(role.getName().name()))).orElse(false);
        if (!owner && !privileged) throw new AccessDeniedException("You are not authorized for this travel hub");
        return r;
    }
    private void requireRole(UserPrincipal p, String... roles) { if (p.getAuthorities().stream().map(GrantedAuthority::getAuthority).noneMatch(a -> List.of(roles).contains(a))) throw new AccessDeniedException("Required role is not authorized"); }
    private BigDecimal zero(BigDecimal n) { return n == null ? BigDecimal.ZERO : n; }
    private TravelHubDto.TravelHubResponse toHub(TravelRequest r) {
        var budget = budgetRepository.findByTravelRequestId(r.getId()).map(this::toBudget).orElse(null);
        var agent = contactRepository.findByTravelRequestId(r.getId()).map(this::toContact).orElse(null);
        var docs = documentRepository.findByTravelRequestIdOrderByCreatedAtDesc(r.getId()).stream().map(d -> new TravelHubDto.DocumentResponse(d.getId(), d.getBooking()==null?null:d.getBooking().getId(), d.getDocumentType(), d.getFileName(), d.getDescription(), "/api/travel-hubs/documents/"+d.getId()+"/download")).collect(Collectors.toList());
        var chat = chatRepository.findByTravelRequestIdOrderByCreatedAtAsc(r.getId()).stream().map(this::toChat).collect(Collectors.toList());
        return new TravelHubDto.TravelHubResponse(r.getId(), r.getRequestNumber(), r.getTripName(), r.getStatus().name(), budget, agent, docs, chat);
    }
    private TravelHubDto.BudgetResponse toBudget(TravelBudget b) { return new TravelHubDto.BudgetResponse(b.getTravelRequest().getId(), b.getAllocatedAmount(), b.getFlightLimit(), b.getHotelLimit(), b.getCabLimit(), b.getMealLimit(), b.getOtherLimit(), b.getStatus()); }
    private TravelHubDto.AgentContactResponse toContact(TravelAgentContact c) { return new TravelHubDto.AgentContactResponse(c.getTravelRequest().getId(), c.getAgencyName(), c.getAgentName(), c.getPhoneNumber(), c.getEmail(), c.getEmergencyPhone(), c.getNotes()); }
    private TravelHubDto.ChatMessageResponse toChat(TravelChatMessage m) { return new TravelHubDto.ChatMessageResponse(m.getId(), m.getSender().getId(), m.getSender().getEmail(), m.getSender().getRoles().stream().map(r -> r.getName().name()).findFirst().orElse("USER"), m.getMessage(), m.getAttachmentName(), m.getCreatedAt()); }
}

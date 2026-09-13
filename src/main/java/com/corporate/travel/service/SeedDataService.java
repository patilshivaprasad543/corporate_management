package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.*;
import com.corporate.travel.entity.enums.*;
import com.corporate.travel.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SeedDataService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SeedDataService.class);

    public SeedDataService(RoleRepository roleRepository, UserRepository userRepository, OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, CostCenterRepository costCenterRepository, EmployeeProfileRepository employeeProfileRepository, TravelPolicyRepository travelPolicyRepository, TravelRequestRepository travelRequestRepository, ApprovalStepRepository approvalStepRepository, BookingRepository bookingRepository, ExpenseReportRepository expenseReportRepository, TravelWalletRepository walletRepository, CorporateCardRepository cardRepository, RiskAlertRepository riskAlertRepository, VendorRepository vendorRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.costCenterRepository = costCenterRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.travelPolicyRepository = travelPolicyRepository;
        this.travelRequestRepository = travelRequestRepository;
        this.approvalStepRepository = approvalStepRepository;
        this.bookingRepository = bookingRepository;
        this.expenseReportRepository = expenseReportRepository;
        this.walletRepository = walletRepository;
        this.cardRepository = cardRepository;
        this.riskAlertRepository = riskAlertRepository;
        this.vendorRepository = vendorRepository;
        this.passwordEncoder = passwordEncoder;
    }


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final CostCenterRepository costCenterRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final TravelPolicyRepository travelPolicyRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final ApprovalStepRepository approvalStepRepository;
    private final BookingRepository bookingRepository;
    private final ExpenseReportRepository expenseReportRepository;
    private final TravelWalletRepository walletRepository;
    private final CorporateCardRepository cardRepository;
    private final RiskAlertRepository riskAlertRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded with demo data.");
            return;
        }

        log.info("Seeding initial corporate demo data for 9 roles and workflows...");

        // 1. Seed Roles
        Map<RoleType, Role> roleMap = new HashMap<>();
        for (RoleType rt : RoleType.values()) {
            Role role = roleRepository.save(Role.builder()
                    .name(rt)
                    .description(rt.name().replace("ROLE_", "").replace("_", " "))
                    .build());
            roleMap.put(rt, role);
        }

        // 2. Organization
        Organization org = organizationRepository.save(Organization.builder()
                .name("Acme Global Technologies Inc.")
                .code("ACME-GLOBAL")
                .domainName("acmetech.com")
                .logoUrl("https://images.unsplash.com/photo-1599305445671-ac291c95aaa9?w=200&auto=format&fit=crop&q=80")
                .city("Hyderabad")
                .state("Telangana")
                .country("India")
                .currencyCode("INR")
                .annualTravelBudget(BigDecimal.valueOf(25000000))
                .active(true)
                .build());

        // 3. Departments
        Department deptEng = departmentRepository.save(Department.builder()
                .name("Engineering & Innovation")
                .code("DEPT-ENG")
                .organization(org)
                .travelBudget(BigDecimal.valueOf(8000000))
                .allocatedBudget(BigDecimal.valueOf(8000000))
                .spentBudget(BigDecimal.valueOf(2400000))
                .build());

        Department deptSales = departmentRepository.save(Department.builder()
                .name("Global Enterprise Sales")
                .code("DEPT-SALES")
                .organization(org)
                .travelBudget(BigDecimal.valueOf(12000000))
                .allocatedBudget(BigDecimal.valueOf(12000000))
                .spentBudget(BigDecimal.valueOf(5600000))
                .build());

        // 4. Cost Centers
        CostCenter ccEng = costCenterRepository.save(CostCenter.builder()
                .name("Core Platform R&D")
                .code("CC-101-ENG")
                .organization(org)
                .budgetLimit(BigDecimal.valueOf(4000000))
                .currentSpend(BigDecimal.valueOf(1200000))
                .build());

        CostCenter ccSales = costCenterRepository.save(CostCenter.builder()
                .name("Enterprise Client Engagements")
                .code("CC-202-SALES")
                .organization(org)
                .budgetLimit(BigDecimal.valueOf(7000000))
                .currentSpend(BigDecimal.valueOf(3100000))
                .build());

        // 5. Travel Policy
        TravelPolicy policy = travelPolicyRepository.save(TravelPolicy.builder()
                .name("Standard Enterprise Travel Policy 2026")
                .description("Default travel rules, budget caps, class eligibilities and tiered approvals")
                .organization(org)
                .maxDomesticFlightPrice(BigDecimal.valueOf(15000))
                .maxInternationalFlightPrice(BigDecimal.valueOf(75000))
                .maxHotelPricePerNight(BigDecimal.valueOf(6000))
                .dailyMealAllowance(BigDecimal.valueOf(2000))
                .dailyTaxiAllowance(BigDecimal.valueOf(1500))
                .advanceBookingDays(7)
                .allowedFlightClass(TravelClass.ECONOMY)
                .allowedHotelCategory(HotelCategory.STANDARD_3_STAR)
                .financeApprovalThreshold(BigDecimal.valueOf(25000))
                .adminApprovalThreshold(BigDecimal.valueOf(100000))
                .requireManagerApproval(true)
                .active(true)
                .build());

        // 6. Users for All 9 Roles
        String defaultPass = passwordEncoder.encode("password123");

        // Super Admin
        User superAdmin = createUser("superadmin", "superadmin@corporatetravel.com", "Alexander", "Pierce",
                defaultPass, org, roleMap.get(RoleType.ROLE_SUPER_ADMIN));

        // Company Admin
        User companyAdmin = createUser("admin", "admin@acmetech.com", "Sarah", "Connor",
                defaultPass, org, roleMap.get(RoleType.ROLE_COMPANY_ADMIN));

        // Manager / Approver
        User manager = createUser("manager", "manager@acmetech.com", "Robert", "Vance",
                defaultPass, org, roleMap.get(RoleType.ROLE_APPROVER));
        deptEng.setManager(manager);
        departmentRepository.save(deptEng);

        // Travel Manager
        User travelManager = createUser("travelmgr", "travelmgr@acmetech.com", "Elena", "Rostova",
                defaultPass, org, roleMap.get(RoleType.ROLE_TRAVEL_MANAGER));

        // Employee
        User employee = createUser("employee", "traveler@acmetech.com", "Priya", "Sharma",
                defaultPass, org, roleMap.get(RoleType.ROLE_EMPLOYEE));

        // Finance User
        User financeUser = createUser("finance", "finance@acmetech.com", "David", "Miller",
                defaultPass, org, roleMap.get(RoleType.ROLE_FINANCE));

        // HR User
        User hrUser = createUser("hr", "hr@acmetech.com", "Rachel", "Green",
                defaultPass, org, roleMap.get(RoleType.ROLE_HR));

        // Vendor Partner
        User vendorUser = createUser("vendor", "partner@indigoair.com", "Vikram", "Malhotra",
                defaultPass, org, roleMap.get(RoleType.ROLE_VENDOR));

        // Support Agent
        User supportAgent = createUser("support", "support@corporatetravel.com", "James", "Wilson",
                defaultPass, org, roleMap.get(RoleType.ROLE_SUPPORT));

        // Employee Profile
        employeeProfileRepository.save(EmployeeProfile.builder()
                .user(employee)
                .employeeCode("EMP-8492")
                .designation("Senior Software Architect")
                .department(deptEng)
                .costCenter(ccEng)
                .manager(manager)
                .allowedTravelClass(TravelClass.ECONOMY)
                .allowedHotelCategory(HotelCategory.STANDARD_3_STAR)
                .preferredAirline("Air India")
                .preferredHotelChain("Taj Hotels")
                .emergencyContactName("Raj Sharma")
                .emergencyContactPhone("+91 98765 43210")
                .build());

        // Travel Wallets
        walletRepository.save(TravelWallet.builder()
                .user(employee)
                .allocatedBudget(BigDecimal.valueOf(350000))
                .approvedBudget(BigDecimal.valueOf(350000))
                .usedBudget(BigDecimal.valueOf(25800))
                .pendingExpenses(BigDecimal.valueOf(3200))
                .reimbursedAmount(BigDecimal.valueOf(18400))
                .corporateCardLimit(BigDecimal.valueOf(200000))
                .currencyCode("INR")
                .build());

        // Corporate Card
        cardRepository.save(CorporateCard.builder()
                .cardToken("tok_corp_visa_9841")
                .lastFourDigits("9841")
                .cardHolderName("PRIYA SHARMA")
                .cardType("VIRTUAL_CORPORATE_VISA")
                .expiryDate(LocalDate.now().plusYears(3))
                .spendingLimit(BigDecimal.valueOf(200000))
                .currentBalance(BigDecimal.valueOf(25800))
                .active(true)
                .user(employee)
                .organization(org)
                .build());

        // 7. Seed Sample Travel Request
        TravelRequest sampleReq = travelRequestRepository.save(TravelRequest.builder()
                .requestNumber("TR-1082")
                .tripName("Annual Tech Summit & Client Architecture Review")
                .tripType(TripType.CLIENT_VISIT)
                .origin("Hyderabad (HYD)")
                .destination("Delhi (DEL)")
                .departureDate(LocalDate.now().plusDays(5))
                .returnDate(LocalDate.now().plusDays(8))
                .roundTrip(true)
                .personalTrip(false)
                .numberOfTravelers(1)
                .businessJustification("In-person client architecture review and enterprise integration sync")
                .clientOrEventName("Global FinTech Solutions Ltd.")
                .estimatedBudget(BigDecimal.valueOf(28000))
                .preferredTravelClass(TravelClass.ECONOMY)
                .preferredHotelCategory(HotelCategory.STANDARD_3_STAR)
                .status(RequestStatus.APPROVED)
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .employee(employee)
                .department(deptEng)
                .costCenter(ccEng)
                .organization(org)
                .build());

        approvalStepRepository.save(ApprovalStep.builder()
                .travelRequest(sampleReq)
                .stepOrder(1)
                .approverRole("ROLE_APPROVER")
                .approver(manager)
                .status(ApprovalStatus.APPROVED)
                .comments("Approved for critical client alignment.")
                .actionTimestamp(LocalDateTime.now().minusDays(1))
                .build());

        approvalStepRepository.save(ApprovalStep.builder()
                .travelRequest(sampleReq)
                .stepOrder(2)
                .approverRole("ROLE_FINANCE")
                .approver(financeUser)
                .status(ApprovalStatus.APPROVED)
                .comments("Budget approved under Q3 R&D cost allocation.")
                .actionTimestamp(LocalDateTime.now().minusHours(12))
                .build());

        // 8. Seed Booking
        Booking sampleBooking = bookingRepository.save(Booking.builder()
                .bookingReference("BK-7492")
                .pnrNumber("PNR683921")
                .bookingType(BookingType.COMBO)
                .status(BookingStatus.CONFIRMED)
                .travelRequest(sampleReq)
                .user(employee)
                .organization(org)
                .totalAmount(BigDecimal.valueOf(20400))
                .taxAmount(BigDecimal.valueOf(2448))
                .currencyCode("INR")
                .paymentMethod(PaymentMethod.CORPORATE_CARD)
                .personalBooking(false)
                .eTicketNumber("ETK-098-8472910")
                .items(new ArrayList<>())
                .build());

        // 9. Seed Expense Report
        ExpenseReport sampleExp = expenseReportRepository.save(ExpenseReport.builder()
                .reportNumber("EXP-3091")
                .title("Delhi Summit Client Meals & Transit")
                .travelRequest(sampleReq)
                .employee(employee)
                .department(deptEng)
                .costCenter(ccEng)
                .status(ExpenseStatus.APPROVED)
                .totalAmount(BigDecimal.valueOf(3200))
                .approvedAmount(BigDecimal.valueOf(3200))
                .currencyCode("INR")
                .hasAiFlags(false)
                .aiReviewNotes("AI Audit: Receipts verified against itinerary timeline. 100% policy compliant.")
                .items(new ArrayList<>())
                .build());

        // 10. Seed Risk Alerts
        riskAlertRepository.save(RiskAlert.builder()
                .destination("Paris, France")
                .countryCode("FR")
                .riskLevel(RiskLevel.MEDIUM)
                .title("Regional Transit Strike Advisory")
                .description("Public transportation disruptions expected near Gare du Nord. Allow extra transit time.")
                .category("TRANSPORTATION")
                .active(true)
                .build());

        riskAlertRepository.save(RiskAlert.builder()
                .destination("Miami, USA")
                .countryCode("US")
                .riskLevel(RiskLevel.LOW)
                .title("Tropical Weather Watch")
                .description("Moderate coastal rainfall; airport operations running normally.")
                .category("WEATHER")
                .active(true)
                .build());

        // 11. Seed Preferred Vendors
        vendorRepository.save(Vendor.builder()
                .name("Air India Corporate")
                .vendorType("AIRLINE")
                .contactEmail("corp@airindia.in")
                .contractNumber("CNT-AI-2026-99")
                .negotiatedDiscountPercent(12.0)
                .slaRating(4.7)
                .preferred(true)
                .organization(org)
                .build());

        vendorRepository.save(Vendor.builder()
                .name("Taj Hotels & Resorts")
                .vendorType("HOTEL_CHAIN")
                .contactEmail("corporate@tajhotels.com")
                .contractNumber("CNT-TAJ-2026-45")
                .negotiatedDiscountPercent(18.0)
                .slaRating(4.9)
                .preferred(true)
                .organization(org)
                .build());

        log.info("Demo data seed completed successfully!");
    }

    private User createUser(String username, String email, String first, String last, String pass, Organization org, Role role) {
        User user = User.builder()
                .username(username)
                .email(email)
                .firstName(first)
                .lastName(last)
                .password(pass)
                .organization(org)
                .roles(new HashSet<>(Collections.singletonList(role)))
                .active(true)
                .emailVerified(true)
                .build();
        return userRepository.save(user);
    }
}

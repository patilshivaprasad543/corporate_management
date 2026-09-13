package com.corporate.travel.service;

import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.entity.Department;
import com.corporate.travel.entity.EmployeeProfile;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.entity.RefreshToken;
import com.corporate.travel.entity.Role;
import com.corporate.travel.entity.TravelWallet;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.PortalType;
import com.corporate.travel.entity.enums.RoleType;
import com.corporate.travel.entity.enums.UserStatus;
import com.corporate.travel.exception.BadRequestException;
import com.corporate.travel.repository.DepartmentRepository;
import com.corporate.travel.repository.EmployeeProfileRepository;
import com.corporate.travel.repository.OrganizationRepository;
import com.corporate.travel.repository.RoleRepository;
import com.corporate.travel.repository.TravelWalletRepository;
import com.corporate.travel.repository.UserRepository;
import com.corporate.travel.security.JwtTokenProvider;
import com.corporate.travel.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final TravelWalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuditService auditService;
    private final PortalAuthorizationService portalAuthorizationService;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;
    private final OrganizationService organizationService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       RoleRepository roleRepository, OrganizationRepository organizationRepository,
                       TravelWalletRepository walletRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider, AuditService auditService,
                       PortalAuthorizationService portalAuthorizationService,
                       RefreshTokenService refreshTokenService, OtpService otpService,
                       OrganizationService organizationService,
                       DepartmentRepository departmentRepository,
                       EmployeeProfileRepository employeeProfileRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.organizationRepository = organizationRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auditService = auditService;
        this.portalAuthorizationService = portalAuthorizationService;
        this.refreshTokenService = refreshTokenService;
        this.otpService = otpService;
        this.organizationService = organizationService;
        this.departmentRepository = departmentRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Transactional
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        String identifier = request.resolveIdentifier();
        PortalType portal = portalAuthorizationService.parsePortal(request.getPortal());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        portalAuthorizationService.validatePortalAccess(user, portal, request.getOrganizationId());

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = refreshTokenService.createRefreshToken(user, portal);

        auditService.logAction(user.getEmail(), "LOGIN", "USER", user.getId(),
                "User logged in via " + portal.name() + " portal", null);

        return buildAuthResponse(user, jwt, refreshToken, portal);
    }

    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (request.getOrganizationId() == null) {
            throw new BadRequestException("Company selection is required");
        }
        if (request.getPassword() == null || request.getConfirmPassword() == null
                || !request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password must match");
        }
        if (request.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        Organization org = organizationService.requireActiveOrganization(request.getOrganizationId());
        validateEmailDomain(request.getEmail(), org);

        String employeeId = request.getEmployeeId().trim();
        if (userRepository.existsByEmployeeIdAndOrganization_Id(employeeId, org.getId())
                || employeeProfileRepository.existsByEmployeeCodeAndUser_Organization_Id(employeeId, org.getId())) {
            throw new BadRequestException("Employee ID is already registered for this company");
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new BadRequestException("Department not found"));
            if (department.getOrganization() == null || !department.getOrganization().getId().equals(org.getId())) {
                throw new BadRequestException("Department does not belong to the selected company");
            }
        }

        Role userRole = roleRepository.findByName(RoleType.ROLE_EMPLOYEE)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(RoleType.ROLE_EMPLOYEE).description("Employee").build()));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .employeeId(employeeId)
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .organization(org)
                .active(true)
                .emailVerified(false)
                .status(UserStatus.PENDING)
                .build();

        User savedUser = userRepository.save(user);

        employeeProfileRepository.save(EmployeeProfile.builder()
                .user(savedUser)
                .employeeCode(employeeId)
                .department(department)
                .manager(department != null ? department.getManager() : null)
                .build());

        walletRepository.save(TravelWallet.builder()
                .user(savedUser)
                .allocatedBudget(BigDecimal.valueOf(300000))
                .approvedBudget(BigDecimal.valueOf(300000))
                .usedBudget(BigDecimal.ZERO)
                .pendingExpenses(BigDecimal.ZERO)
                .corporateCardLimit(BigDecimal.valueOf(200000))
                .currencyCode("INR")
                .build());

        otpService.generateAndSendOtp(savedUser);

        auditService.logAction(savedUser.getEmail(), "REGISTER", "USER", savedUser.getId(),
                "New employee registered — OTP sent for verification", null);

        return AuthDto.AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .organizationId(org != null ? org.getId() : null)
                .organizationName(org != null ? org.getName() : null)
                .roles(savedUser.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
                .emailVerified(false)
                .message("Registration successful. Please verify your email with the OTP sent.")
                .build();
    }

    @Transactional
    public void verifyOtp(AuthDto.OtpVerifyRequest request) {
        otpService.verifyOtp(request.getEmail(), request.getOtp());
        auditService.logAction(request.getEmail(), "OTP_VERIFY", "USER", null,
                "Email verified via OTP", null);
    }

    @Transactional
    public void resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new BadRequestException("Email is already verified");
        }
        otpService.generateAndSendOtp(user);
    }

    @Transactional
    public AuthDto.AuthResponse refresh(AuthDto.RefreshRequest request) {
        RefreshToken stored = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = stored.getUser();

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BadRequestException("Account is disabled");
        }

        UserPrincipal principal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
        String jwt = tokenProvider.generateToken(authentication);
        String newRefresh = refreshTokenService.createRefreshToken(user,
                stored.getPortal() != null ? PortalType.valueOf(stored.getPortal()) : null);
        refreshTokenService.revokeToken(stored);

        return buildAuthResponse(user, jwt, newRefresh, null);
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            try {
                RefreshToken stored = refreshTokenService.validateRefreshToken(refreshToken);
                refreshTokenService.revokeToken(stored);
                auditService.logAction(stored.getUser().getEmail(), "LOGOUT", "USER",
                        stored.getUser().getId(), "User logged out", null);
            } catch (Exception ignored) {
                // Token may already be invalid
            }
        }
        SecurityContextHolder.clearContext();
    }

    public AuthDto.MeResponse getCurrentUser(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        Set<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(p -> p.getName().name())
                .collect(Collectors.toSet());

        return AuthDto.MeResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : null)
                .roles(user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
                .permissions(permissions)
                .emailVerified(user.getEmailVerified())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    private AuthDto.AuthResponse buildAuthResponse(User user, String jwt, String refreshToken, PortalType portal) {
        return AuthDto.AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : "Enterprise SaaS")
                .roles(user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
                .portal(portal != null ? portal.name() : null)
                .emailVerified(user.getEmailVerified())
                .build();
    }

    private void validateEmailDomain(String email, Organization org) {
        if (org.getDomainName() == null || org.getDomainName().isBlank()) {
            return;
        }
        String domain = org.getDomainName().trim().toLowerCase();
        String emailDomain = email.substring(email.indexOf('@') + 1).trim().toLowerCase();
        if (!emailDomain.equals(domain)) {
            throw new BadRequestException("Email must use your company domain: @" + domain);
        }
    }
}

package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.entity.Role;
import com.corporate.travel.entity.TravelWallet;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.RoleType;
import com.corporate.travel.exception.BadRequestException;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, OrganizationRepository organizationRepository, TravelWalletRepository walletRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, AuditService auditService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.organizationRepository = organizationRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auditService = auditService;
    }


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final TravelWalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuditService auditService;

    @Transactional
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        auditService.logAction(user.getEmail(), "LOGIN", "USER", user.getId(), "User logged in successfully", null);

        return AuthDto.AuthResponse.builder()
                .accessToken(jwt)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : "Enterprise SaaS")
                .roles(user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
                .build();
    }

    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        // Public registration may only create a traveler. Elevated roles are
        // provisioned by an organization administrator through a controlled
        // user-management workflow.
        RoleType roleType = RoleType.ROLE_EMPLOYEE;

        Role userRole = roleRepository.findByName(roleType)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ROLE_EMPLOYEE).description("Employee").build()));

        Organization org = null;
        if (request.getOrganizationId() != null) {
            org = organizationRepository.findById(request.getOrganizationId()).orElse(null);
        }
        if (org == null) {
            org = organizationRepository.findAll().stream().findFirst().orElse(null);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .organization(org)
                .active(true)
                .emailVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        // Initialize Travel Wallet
        walletRepository.save(TravelWallet.builder()
                .user(savedUser)
                .allocatedBudget(BigDecimal.valueOf(300000))
                .approvedBudget(BigDecimal.valueOf(300000))
                .usedBudget(BigDecimal.ZERO)
                .pendingExpenses(BigDecimal.ZERO)
                .corporateCardLimit(BigDecimal.valueOf(200000))
                .currencyCode("INR")
                .build());

        auditService.logAction(savedUser.getEmail(), "REGISTER", "USER", savedUser.getId(), "New user registered with role " + roleType.name(), null);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserPrincipal.create(savedUser), null, UserPrincipal.create(savedUser).getAuthorities());
        String jwt = tokenProvider.generateToken(authentication);

        return AuthDto.AuthResponse.builder()
                .accessToken(jwt)
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .organizationId(org != null ? org.getId() : null)
                .organizationName(org != null ? org.getName() : "Enterprise SaaS")
                .roles(savedUser.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
                .build();
    }
}

package com.benjaminbatte.platform.features.org.service.impl;

import com.benjaminbatte.platform.common.exception.ResourceNotFoundException;
import com.benjaminbatte.platform.common.exception.UnauthorizedException;
import com.benjaminbatte.platform.common.security.CurrentUser;
import com.benjaminbatte.platform.features.org.domain.Org;
import com.benjaminbatte.platform.features.org.domain.Role;
import com.benjaminbatte.platform.features.org.domain.UserOrgRole;
import com.benjaminbatte.platform.features.org.dto.AddUserToOrgRequest;
import com.benjaminbatte.platform.features.org.dto.UserOrgRoleDto;
import com.benjaminbatte.platform.features.org.mapper.UserOrgRoleMapper;
import com.benjaminbatte.platform.features.org.repo.OrgRepository;
import com.benjaminbatte.platform.features.org.repo.UserOrgRoleRepository;
import com.benjaminbatte.platform.features.org.service.OrgMembershipService;
import com.benjaminbatte.platform.features.user.domain.User;
import com.benjaminbatte.platform.features.user.repo.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrgMembershipServiceImpl implements OrgMembershipService {

    private final OrgRepository orgRepo;
    private final UserRepository userRepo;
    private final UserOrgRoleRepository uorRepo;

    @Override
    @Transactional
    public UserOrgRoleDto addUser(UUID orgId, @Valid AddUserToOrgRequest request) {
        String externalId = currentExternalIdOrThrow();          // 401 if missing
        Org org = findOrgOrThrow(orgId);                         // 404 if missing
        assertAdminInOrgOrThrow(externalId, orgId);              // 403 if not admin

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        // Upsert membership (unique (user, org))
        UserOrgRole uor = uorRepo.findByUser_IdAndOrg_Id(user.getId(), org.getId())
                .orElseGet(() -> UserOrgRole.builder()
                        .user(user)
                        .org(org)
                        .grantedAt(Instant.now())
                        .build());

        uor.setRole(request.getRole());
        return UserOrgRoleMapper.toDto(uorRepo.save(uor));
    }

    @Override
    public List<UserOrgRoleDto> listMembers(UUID orgId) {
        String externalId = currentExternalIdOrThrow();          // 401
        findOrgOrThrow(orgId);                                   // 404

        // Require membership to view list (tighten to ADMIN if you prefer)
        boolean isMember = uorRepo.existsByUser_ExternalIdAndOrg_Id(externalId, orgId);
        if (!isMember) throw new AccessDeniedException("You are not a member of this organization");

        return uorRepo.findAllByOrg_Id(orgId).stream()
                .map(UserOrgRoleMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void changeRole(UUID orgId, UUID userId, Role role) {
        String externalId = currentExternalIdOrThrow();          // 401
        findOrgOrThrow(orgId);                                   // 404
        assertAdminInOrgOrThrow(externalId, orgId);              // 403

        UserOrgRole uor = uorRepo.findByUser_IdAndOrg_Id(userId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Membership not found for user %s in org %s".formatted(userId, orgId)));

        uor.setRole(role);
        uorRepo.save(uor);
    }

    @Override
    @Transactional
    public void removeMember(UUID orgId, UUID userId) {
        String externalId = currentExternalIdOrThrow();          // 401
        findOrgOrThrow(orgId);                                   // 404
        assertAdminInOrgOrThrow(externalId, orgId);              // 403

        UserOrgRole uor = uorRepo.findByUser_IdAndOrg_Id(userId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Membership not found for user %s in org %s".formatted(userId, orgId)));

        uorRepo.delete(uor);
    }

    // -------- helpers --------

    private Org findOrgOrThrow(UUID orgId) {
        return orgRepo.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Org not found: " + orgId));
    }

    /** Uses Authentication.getName() which your JwtAuthConverter set (preferred_username or sub). */
    private String currentExternalIdOrThrow() {
        return CurrentUser.externalIdOrThrow();
    }

    private void assertAdminInOrgOrThrow(String externalId, UUID orgId) {
        boolean isAdmin = uorRepo.existsByUser_ExternalIdAndOrg_IdAndRoleIn(
                externalId, orgId, EnumSet.of(Role.ADMIN));
        if (!isAdmin) {
            throw new AccessDeniedException("Not an admin of this organization");
        }
    }
}

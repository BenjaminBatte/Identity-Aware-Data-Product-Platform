package com.benjaminbatte.platform.features.org.repo;

import com.benjaminbatte.platform.features.org.domain.Role;
import com.benjaminbatte.platform.features.org.domain.UserOrgRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserOrgRoleRepository extends JpaRepository<UserOrgRole, UUID> {

    // --- Existing methods ---
    Optional<UserOrgRole> findByUser_IdAndOrg_Id(UUID userId, UUID orgId);

    boolean existsByUser_IdAndOrg_IdAndRoleIn(
            UUID userId,
            UUID orgId,
            Collection<Role> roles
    );

    // --- Useful additions ---

    // 1) “Is member at all?” (no role filter)
    boolean existsByUser_IdAndOrg_Id(UUID userId, UUID orgId);

    // 2) Token-driven checks using OIDC subject (externalId) – perfect for @PreAuthorize
    boolean existsByUser_ExternalIdAndOrg_IdAndRoleIn(
            String externalId,
            UUID orgId,
            Collection<Role> roles
    );

    boolean existsByUser_ExternalIdAndOrg_Id(String externalId, UUID orgId);

    // 3) Quick role lookup (tiny select)
    @Query("select uor.role from UserOrgRole uor where uor.user.id = :userId and uor.org.id = :orgId")
    Optional<Role> findRole(UUID userId, UUID orgId);

    // 4) Lists for admin pages
    List<UserOrgRole> findAllByOrg_Id(UUID orgId);
    List<UserOrgRole> findAllByOrg_IdAndRole(UUID orgId, Role role);
    List<UserOrgRole> findAllByUser_Id(UUID userId);

    // 5) Simple metrics
    long countByOrg_IdAndRole(UUID orgId, Role role);
}

package com.benjaminbatte.platform.features.org.repo;

import com.benjaminbatte.platform.features.org.domain.UserOrgRole;
import com.benjaminbatte.platform.features.org.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserOrgRoleRepository extends JpaRepository<UserOrgRole, UUID> {

    Optional<UserOrgRole> findByUser_IdAndOrg_Id(UUID userId, UUID orgId);

    boolean existsByUser_IdAndOrg_IdAndRoleIn(
            UUID userId,
            UUID orgId,
            Collection<Role> roles
    );
}

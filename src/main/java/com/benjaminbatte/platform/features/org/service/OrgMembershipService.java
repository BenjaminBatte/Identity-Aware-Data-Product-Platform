package com.benjaminbatte.platform.features.org.service;

import com.benjaminbatte.platform.features.org.domain.Role;
import com.benjaminbatte.platform.features.org.dto.AddUserToOrgRequest;
import com.benjaminbatte.platform.features.org.dto.UserOrgRoleDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Org-scoped membership management.
 * Implementations must enforce: 401 (no auth), 403 (not allowed), 404 (missing resources).
 */
public interface OrgMembershipService {

    /** Add or update a user's role in an org (upsert membership). Requires ADMIN in that org. */
    @NotNull UserOrgRoleDto addUser(@NotNull UUID orgId, @Valid @NotNull AddUserToOrgRequest request);

    /** List all members for an org. Requires caller to be a member of that org. */
    @NotNull List<UserOrgRoleDto> listMembers(@NotNull UUID orgId);

    /** Change a user's role in an org. Requires ADMIN in that org. */
    void changeRole(@NotNull UUID orgId, @NotNull UUID userId, @NotNull Role role);

    /** Remove a user from an org. Requires ADMIN in that org. */
    void removeMember(@NotNull UUID orgId, @NotNull UUID userId);
}

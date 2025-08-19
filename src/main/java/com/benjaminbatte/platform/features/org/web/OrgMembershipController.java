package com.benjaminbatte.platform.features.org.web;

import com.benjaminbatte.platform.common.web.versioning.ApiV1;          // <-- marker to attach /v1 prefix
import com.benjaminbatte.platform.features.org.domain.Role;
import com.benjaminbatte.platform.features.org.dto.AddUserToOrgRequest;
import com.benjaminbatte.platform.features.org.dto.UserOrgRoleDto;
import com.benjaminbatte.platform.features.org.service.OrgMembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Org membership endpoints (versioned).
 *
 * Versioning strategy:
 *  - server.servlet.context-path=/api  (from application.yml)
 *  - @ApiV1 marker + ApiVersioningConfig addPathPrefix("/v1", c -> c.isAnnotationPresent(ApiV1.class))
 *  - This class is annotated with @ApiV1, so the final base path becomes: /api/v1/...
 *
 * Resulting routes:
 *   POST   /api/v1/orgs/{orgId}/members                 -> add member (ADMIN only)
 *   GET    /api/v1/orgs/{orgId}/members                 -> list members (must be a member; service enforces)
 *   PUT    /api/v1/orgs/{orgId}/members/{userId}/role/{role}  -> change role (ADMIN only)
 *   DELETE /api/v1/orgs/{orgId}/members/{userId}        -> remove member (ADMIN only)
 */
@RestController
@ApiV1
@Validated
@RequiredArgsConstructor
@RequestMapping("/orgs/{orgId}/members") // Do NOT put /api or /v1 here; those are added by context-path + @ApiV1
public class OrgMembershipController {

    private final OrgMembershipService service;

    // Require org admins (global ADMIN is fine too if your roles are global); the service also enforces org-scope.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOrgRoleDto addMember(@PathVariable UUID orgId,
                                    @Valid @RequestBody AddUserToOrgRequest request) {
        return service.addUser(orgId, request);
    }

    // Anyone in the org can list members (ADMIN/ANALYST/VIEWER). Service validates caller belongs to the org.
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST') or hasRole('VIEWER')")
    @GetMapping
    public List<UserOrgRoleDto> list(@PathVariable UUID orgId) {
        return service.listMembers(orgId);
    }

    // Change a user's role. Protected both here (ADMIN) and in the service (org-scoped).
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/role/{role}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeRole(@PathVariable UUID orgId,
                           @PathVariable UUID userId,
                           @PathVariable Role role) {
        service.changeRole(orgId, userId, role);
    }

    // Remove a user from the org. Guarded against removing the last admin inside the service.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable UUID orgId,
                             @PathVariable UUID userId) {
        service.removeMember(orgId, userId);
    }
}

package com.benjaminbatte.platform.features.org.dto;

import com.benjaminbatte.platform.features.org.domain.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddUserToOrgRequest {
    @NotNull UUID userId; // orgId comes from the URL path
    @NotNull Role role;
}

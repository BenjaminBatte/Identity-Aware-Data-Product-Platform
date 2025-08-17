package com.benjaminbatte.platform.features.org.dto;

import com.benjaminbatte.platform.features.org.domain.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class AddUserToOrgRequest {
    @NotNull UUID userId;
    @NotNull Role role;
}

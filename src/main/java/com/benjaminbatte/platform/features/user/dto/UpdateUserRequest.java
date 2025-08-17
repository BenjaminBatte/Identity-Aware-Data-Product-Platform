package com.benjaminbatte.platform.features.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateUserRequest {
    @Size(min = 1, max = 200)
    String displayName;
}

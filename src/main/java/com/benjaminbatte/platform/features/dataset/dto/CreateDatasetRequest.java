
package com.benjaminbatte.platform.features.dataset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class CreateDatasetRequest {
    @NotNull UUID orgId;
    @NotBlank String name;
    String description;
    Set<String> tags;
}

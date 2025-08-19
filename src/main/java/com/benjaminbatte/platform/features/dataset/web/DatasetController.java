package com.benjaminbatte.platform.features.dataset.web;

import com.benjaminbatte.platform.features.dataset.dto.CreateDatasetRequest;
import com.benjaminbatte.platform.features.dataset.dto.DatasetDto;
import com.benjaminbatte.platform.common.exception.ResourceNotFoundException;
import com.benjaminbatte.platform.features.user.repo.UserRepository;
import com.benjaminbatte.platform.features.dataset.service.DatasetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/datasets")
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetService datasetService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DatasetDto create(@Valid @RequestBody CreateDatasetRequest req,
                             @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {

            email = jwt.getClaimAsString("preferred_username");
        }
        String finalEmail = email;
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not registered: " + finalEmail));

        DatasetDto dto = datasetService.create(req, user.getId());
        return dto;
    }
}

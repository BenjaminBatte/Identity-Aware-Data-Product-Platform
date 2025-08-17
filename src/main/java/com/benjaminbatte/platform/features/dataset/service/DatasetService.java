package com.benjaminbatte.platform.features.dataset.service;

import com.benjaminbatte.platform.features.dataset.dto.CreateDatasetRequest;
import com.benjaminbatte.platform.features.dataset.dto.DatasetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DatasetService {
    DatasetDto create(CreateDatasetRequest req, UUID ownerUserId);
    DatasetDto getById(UUID id);
    Page<DatasetDto> listByOrg(UUID orgId, Pageable pageable);
}

package com.benjaminbatte.platform.features.dataset.service.impl;

import com.benjaminbatte.platform.common.exception.PlatformException;
import com.benjaminbatte.platform.common.exception.ResourceNotFoundException;
import com.benjaminbatte.platform.features.dataset.domain.Dataset;
import com.benjaminbatte.platform.features.dataset.dto.CreateDatasetRequest;
import com.benjaminbatte.platform.features.dataset.dto.DatasetDto;
import com.benjaminbatte.platform.features.dataset.mapper.DatasetMapper;
import com.benjaminbatte.platform.features.dataset.repo.DatasetRepository;
import com.benjaminbatte.platform.features.dataset.service.DatasetService;
import com.benjaminbatte.platform.features.org.domain.Org;
import com.benjaminbatte.platform.features.org.domain.Role;
import com.benjaminbatte.platform.features.org.repo.OrgRepository;
import com.benjaminbatte.platform.features.org.repo.UserOrgRoleRepository;
import com.benjaminbatte.platform.features.user.domain.User;
import com.benjaminbatte.platform.features.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DatasetServiceImpl implements DatasetService {

    private final OrgRepository orgRepo;
    private final UserRepository userRepo;
    private final UserOrgRoleRepository uorRepo;
    private final DatasetRepository datasetRepo;

    @Override
    public DatasetDto create(CreateDatasetRequest req, UUID ownerUserId) {
        Org org = orgRepo.findById(req.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Org not found: " + req.getOrgId()));

        User owner = userRepo.findById(ownerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + ownerUserId));

        boolean permitted = uorRepo.existsByUser_IdAndOrg_IdAndRoleIn(
                owner.getId(), org.getId(), EnumSet.of(Role.ADMIN, Role.ANALYST));
        if (!permitted) {
            throw new PlatformException("User not permitted to create datasets in this org", "FORBIDDEN") {};
        }

        datasetRepo.findByOrg_IdAndName(org.getId(), req.getName())
                .ifPresent(d -> { throw new PlatformException("Dataset name already exists in this org", "DATASET_DUPLICATE") {}; });

        Dataset d = Dataset.builder()
                .org(org)
                .owner(owner)
                .name(req.getName())
                .description(req.getDescription())
                .tags(req.getTags() != null ? new HashSet<>(req.getTags()) : new HashSet<>())
                .build();

        return DatasetMapper.toDto(datasetRepo.save(d));
    }

    @Override
    @Transactional(readOnly = true)
    public DatasetDto getById(UUID id) {
        Dataset d = datasetRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dataset not found: " + id));
        return DatasetMapper.toDto(d);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DatasetDto> listByOrg(UUID orgId, Pageable pageable) {
        Specification<Dataset> spec = (root, q, cb) -> cb.equal(root.get("org").get("id"), orgId);
        return datasetRepo.findAll(spec, pageable).map(DatasetMapper::toDto);
    }
}

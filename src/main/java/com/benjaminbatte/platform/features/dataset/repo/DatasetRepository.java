package com.benjaminbatte.platform.features.dataset.repo;

import com.benjaminbatte.platform.features.dataset.domain.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface DatasetRepository
        extends JpaRepository<Dataset, UUID>, JpaSpecificationExecutor<Dataset> {

    Optional<Dataset> findByOrg_IdAndName(UUID orgId, String name);
}

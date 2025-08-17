package com.benjaminbatte.platform.features.dataset.repo;

import com.benjaminbatte.platform.features.dataset.domain.DatasetVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DatasetVersionRepository extends JpaRepository<DatasetVersion, UUID> {
    Optional<DatasetVersion> findTop1ByDataset_IdOrderByVersionDesc(UUID datasetId);
}

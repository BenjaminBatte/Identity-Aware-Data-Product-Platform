package com.benjaminbatte.platform.features.dataset.repo;

import com.benjaminbatte.platform.features.dataset.domain.DatasetProfileRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DatasetProfileRefRepository extends JpaRepository<DatasetProfileRef, UUID> { }

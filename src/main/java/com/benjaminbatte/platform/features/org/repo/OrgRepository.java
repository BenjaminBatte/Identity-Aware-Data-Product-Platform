package com.benjaminbatte.platform.features.org.repo;
import com.benjaminbatte.platform.features.org.domain.Org;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrgRepository extends JpaRepository<Org, UUID> {
    Optional<Org> findBySlug(String slug);
}

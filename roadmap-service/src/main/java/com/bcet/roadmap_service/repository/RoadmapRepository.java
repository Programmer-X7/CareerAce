package com.bcet.roadmap_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcet.roadmap_service.model.Roadmap;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, UUID> {

    Optional<Roadmap> findBySlug(String slug);
}

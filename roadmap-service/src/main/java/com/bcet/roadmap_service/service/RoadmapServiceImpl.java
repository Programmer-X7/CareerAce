package com.bcet.roadmap_service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bcet.roadmap_service.dto.RoadmapCard;
import com.bcet.roadmap_service.model.Roadmap;
import com.bcet.roadmap_service.repository.RoadmapRepository;

@Service
public class RoadmapServiceImpl implements RoadmapService {

    private final RoadmapRepository repository;

    public RoadmapServiceImpl(RoadmapRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addRoadmap(Roadmap roadmap) {
        try {
            repository.save(roadmap);
        } catch (Exception e) {
            throw new RuntimeException("Error saving roadmap: " + e.getMessage());
        }
    }

    @Override
    public List<RoadmapCard> getAllRoadmaps() {
        try {
            return repository.findAll().stream()
                    .map(roadmap -> new RoadmapCard(
                            roadmap.getRoadmapId().toString(),
                            roadmap.getSlug(),
                            roadmap.getTitle(),
                            roadmap.getThumbnail(),
                            roadmap.getSubTitle(),
                            roadmap.getCategory().getName()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching roadmaps: " + e.getMessage());
        }
    }

    @Override
    public Roadmap getRoadmapBySlug(String slug) {
        try {
            Optional<Roadmap> roadmap = repository.findBySlug(slug);
            if (!roadmap.isPresent()) {
                throw new RuntimeException("Roadmap not found: " + slug);
            }
            return roadmap.get();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching roadmaps: " + e.getMessage());
        }
    }

    @Override
    public void updateRoadmap(String id, Roadmap roadmap) {
        Roadmap existingRoadmap = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Roadmap not found with id " + id));

        // Update fields only if they are non-null
        if (roadmap.getSlug() != null)
            existingRoadmap.setSlug(roadmap.getSlug());
        if (roadmap.getTitle() != null)
            existingRoadmap.setTitle(roadmap.getTitle());
        if (roadmap.getThumbnail() != null)
            existingRoadmap.setThumbnail(roadmap.getThumbnail());
        if (roadmap.getSubTitle() != null)
            existingRoadmap.setSubTitle(roadmap.getSubTitle());
        if (roadmap.getDescription() != null)
            existingRoadmap.setDescription(roadmap.getDescription());
        if (roadmap.getImage() != null)
            existingRoadmap.setImage(roadmap.getImage());
        if (roadmap.getContent() != null)
            existingRoadmap.setContent(roadmap.getContent());
        if (roadmap.getCategory() != null)
            existingRoadmap.setCategory(roadmap.getCategory());

        try {
            repository.save(existingRoadmap);
        } catch (Exception e) {
            throw new RuntimeException("Error updating roadmap");
        }
    }

    @Override
    public void deleteRoadmap(String id) {
        try {
            if (!repository.existsById(UUID.fromString(id))) {
                throw new RuntimeException("Roadmap not found: " + id);
            }
            repository.deleteById(UUID.fromString(id));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting roadmap: " + e.getMessage());
        }
    }

}

package com.bcet.roadmap_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcet.roadmap_service.dto.ApiResponse;
import com.bcet.roadmap_service.dto.RoadmapCard;
import com.bcet.roadmap_service.model.Roadmap;
import com.bcet.roadmap_service.service.RoadmapService;

@RestController
@RequestMapping("/roadmaps")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    // Add Roadmap
    @PostMapping
    public ResponseEntity<ApiResponse> addRoadmap(@RequestBody Roadmap roadmap) {
        try {
            roadmapService.addRoadmap(roadmap);
            return new ResponseEntity<>(new ApiResponse("Roadmap successfully added"), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error adding roadmap!"));
        }
    }

    // Get All Roadmaps (List<roadmapId, slug, thumbnail, title, subtitle,
    // category>)
    @GetMapping
    public ResponseEntity<?> getAllRoadmaps() {
        try {
            List<RoadmapCard> roadmaps = roadmapService.getAllRoadmaps();
            return ResponseEntity.ok(roadmaps);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error fetching roadmaps"));
        }

    }

    // Get Roadmap Details by Slug
    @GetMapping("/{slug}")
    public ResponseEntity<?> getRoadmapDetails(@PathVariable String slug) {
        try {
            return ResponseEntity.ok(roadmapService.getRoadmapBySlug(slug));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error fetching roadmap: " + slug));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoadmap(@PathVariable String id, @RequestBody Roadmap roadmap) {
        try {
            roadmapService.updateRoadmap(id, roadmap);
            return ResponseEntity.ok(new ApiResponse("Roadmap successfully updated."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error updating roadmap."));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteRoadmap(@PathVariable String id) {
        try {
            roadmapService.deleteRoadmap(id);
            return new ResponseEntity<>(new ApiResponse("Roadmap successfully deleted"), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error deleting roadmap: " + id));
        }
    }

}

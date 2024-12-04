package com.bcet.roadmap_service.service;

import java.util.List;

import com.bcet.roadmap_service.dto.RoadmapCard;
import com.bcet.roadmap_service.model.Roadmap;

public interface RoadmapService {

    void addRoadmap(Roadmap roadmap);

    List<RoadmapCard> getAllRoadmaps();

    Roadmap getRoadmapBySlug(String slug);
    
    void updateRoadmap(String id, Roadmap roadmap);

    void deleteRoadmap(String id);


}

package com.bcet.roadmap_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoadmapCard {

    private String roadmapId;
    private String slug;
    private String title;
    private String thumbnail;
    private String subTitle;
    private String category;

}

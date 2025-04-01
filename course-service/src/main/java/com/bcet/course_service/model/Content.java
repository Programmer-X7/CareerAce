package com.bcet.course_service.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID contentId;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private int contentOrder;

    @Column(columnDefinition = "TEXT")
    private String videoURL;

    @Column(columnDefinition = "TEXT")
    private String articleURL;

    @Column(nullable = false)
    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    @JsonIgnore
    private Section section;

}

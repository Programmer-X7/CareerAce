package com.bcet.course_service.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID testimonialId;

    @Column(nullable = false)
    private String userId;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(nullable = false)
    private float rating;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

}

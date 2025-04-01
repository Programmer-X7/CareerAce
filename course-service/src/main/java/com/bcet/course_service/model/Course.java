package com.bcet.course_service.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID courseId;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String thumbnail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private float rating;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Testimonial> testimonials;

    @Column(nullable = false)
    private int originalPrice;

    @Column(nullable = false)
    private float discount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String features;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String highlights;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

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

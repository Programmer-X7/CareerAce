package com.bcet.resume_service.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class Resume {

    @Id
    @Column(nullable = false, unique = true)
    private String resumeId;

    @Column(nullable = false)
    private String templateId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String themeColor;

    private String firstName;

    private String lastName;

    private String jobTitle;

    private String address;

    private String email;

    private String phone;

    private String linkedIn;

    private String github;

    private String portfolio;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experience;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> education;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages;

    private String interests;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}

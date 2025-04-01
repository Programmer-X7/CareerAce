package com.bcet.mock_test_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "slug", "category_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "category")
public class TestSet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String testSetId;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private long attempts;

    @OneToMany(mappedBy = "testSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

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

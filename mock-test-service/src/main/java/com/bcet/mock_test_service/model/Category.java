package com.bcet.mock_test_service.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String categoryId;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private long popularity;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TestSet> testSets;

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

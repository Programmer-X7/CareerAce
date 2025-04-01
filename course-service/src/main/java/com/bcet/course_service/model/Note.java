package com.bcet.course_service.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "notes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "userId", "contentId" })
})
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID noteId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private UUID contentId;

    @Column(columnDefinition = "TEXT")
    private String noteText;

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}

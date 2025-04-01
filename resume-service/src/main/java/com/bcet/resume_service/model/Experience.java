package com.bcet.resume_service.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@ToString(exclude = "resume")
public class Experience {

    @Id
    private String id;

    private String title;

    private String companyName;

    private String city;

    private String state;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentlyWorking;

    @Column(columnDefinition = "TEXT")
    private String workSummary;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    @JsonIgnore
    private Resume resume;
}

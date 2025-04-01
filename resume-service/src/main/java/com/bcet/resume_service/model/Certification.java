package com.bcet.resume_service.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Certification {

    @Id
    private String id;

    private String title;

    private String issuer;

    private LocalDate issueDate;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    @JsonIgnore
    private Resume resume;
}

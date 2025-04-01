package com.bcet.mock_test_service.model;

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
@ToString(exclude = "question")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String optionId;

    @Column(nullable = false)
    private String optionText;

    @Column(nullable = false)
    private int optionOrder;

    @Column(nullable = false)
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;
}

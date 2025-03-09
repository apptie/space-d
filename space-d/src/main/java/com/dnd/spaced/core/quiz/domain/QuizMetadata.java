package com.dnd.spaced.core.quiz.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Table(name = "quiz_metadata")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
public class QuizMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long totalQuizQuestionCount = 0L;
    private long totalTodayQuizQuestionCount = 0L;
}

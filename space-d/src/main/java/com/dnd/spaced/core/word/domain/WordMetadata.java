package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.global.audit.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
public class WordMetadata extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalWordCount;
    private int businessWordCount;
    private int developWordCount;
    private int designWordCount;

    public WordMetadata() {
        this.totalWordCount = 0;
        this.businessWordCount = 0;
        this.developWordCount = 0;
        this.designWordCount = 0;
    }

    public void addBusinessWordCount() {
        this.businessWordCount++;
        this.totalWordCount++;
    }

    public void addDevelopWordCount() {
        this.developWordCount++;
        this.totalWordCount++;
    }

    public void addDesignWordCount() {
        this.designWordCount++;
        this.totalWordCount++;
    }

    public boolean canGenerateBusinessQuiz(int requiredWordCount) {
        return businessWordCount >= requiredWordCount;
    }

    public boolean canGenerateDesignQuiz(int requiredWordCount) {
        return designWordCount >= requiredWordCount;
    }

    public boolean canGenerateDevelopQuiz(int requiredWordCount) {
        return developWordCount >= requiredWordCount;
    }

    public boolean canGenerateTotalQuiz(int requiredWordCount) {
        return totalWordCount >= requiredWordCount;
    }
}

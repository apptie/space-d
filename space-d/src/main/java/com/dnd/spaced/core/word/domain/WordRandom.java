package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.enums.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "word_randoms")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WordRandom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wordId;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int random;

    public WordRandom(Long wordId, Category category,int random) {
        this.wordId = wordId;
        this.category = category;
        this.random = random;
    }
}

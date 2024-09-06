package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.exception.InvalidWordNameException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "words")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Embedded
    private WordMeaning wordMeaning;

    @OneToMany(mappedBy = "word", cascade = CascadeType.PERSIST)
    private List<Pronunciation> pronunciations = new ArrayList<>();

    @OneToMany(mappedBy = "word", cascade = CascadeType.PERSIST)
    private List<WordExample> wordExamples = new ArrayList<>();

    private long viewCount = 0L;

    @Builder
    private Word(String name, String meaning, String categoryName) {
        validateContent(name);

        this.name = name;
        this.wordMeaning = new WordMeaning(meaning);
        this.category = Category.findBy(categoryName);
    }

    public void addPronunciation(Pronunciation pronunciation) {
        pronunciation.initWord(this);
        this.pronunciations.add(pronunciation);
    }

    public void addWordExample(WordExample wordExample) {
        wordExample.initWord(this);
        this.wordExamples.add(wordExample);
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void changeMeaning(String meaning) {
        this.wordMeaning.changeMeaning(meaning);
    }

    private void validateContent(String name) {
        if (isInvalidName(name)) {
            throw new InvalidWordNameException("용어 이름은 null이거나 비어 있을 수 없습니다.");
        }
    }

    private boolean isInvalidName(String name) {
        return name == null || name.isBlank();
    }
}

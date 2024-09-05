package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.exception.InvalidWordExampleContentException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class WordExample {

    private static final int MIN_EXAMPLE_LENGTH = 1;
    private static final int MAX_EXAMPLE_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String example;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    public WordExample(String example) {
        validateContent(example);

        this.example = example;
    }

    public void initWord(Word word) {
        this.word = word;
    }

    public void changeExample(String changedExample) {
        validateContent(changedExample);

        this.example = changedExample;
    }

    public boolean isEqualTo(Long id) {
        return this.id.equals(id);
    }

    private void validateContent(String content) {
        if (isInvalidContent(content)) {
            throw new InvalidWordExampleContentException("예문의 길이는 최소 1글자 이상, 최대 50글자 이하여야 합니다.");
        }
    }

    private boolean isInvalidContent(String content) {
        return content == null || content.isBlank()
                || MIN_EXAMPLE_LENGTH > content.length() || MAX_EXAMPLE_LENGTH < content.length();
    }
}

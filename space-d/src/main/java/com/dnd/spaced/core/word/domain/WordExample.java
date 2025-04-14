package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.exception.InvalidWordExampleContentException;
import com.dnd.spaced.global.audit.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "word_examples")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class WordExample extends BaseTimeEntity {

    private static final int MIN_EXAMPLE_LENGTH = 1;
    private static final int MAX_EXAMPLE_LENGTH = 150;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    public static WordExample from(String content) {
        validateContent(content);

        return new WordExample(content);
    }

    private static void validateContent(String content) {
        if (isInvalidContent(content)) {
            throw new InvalidWordExampleContentException("예문의 길이는 최소 1글자 이상, 최대 150글자 이하여야 합니다.");
        }
    }

    private static boolean isInvalidContent(String content) {
        return content == null || content.isBlank()
                || MIN_EXAMPLE_LENGTH > content.length() || MAX_EXAMPLE_LENGTH < content.length();
    }

    private WordExample(String content) {
        this.content = content;
    }

    public void initWord(Word word) {
        this.word = word;
    }

    public void changeExample(String changedExample) {
        validateContent(changedExample);

        this.content = changedExample;
    }

    public void deleted() {
        this.deleted = true;
    }

    public boolean isEqualTo(Long id) {
        return this.id.equals(id);
    }
}

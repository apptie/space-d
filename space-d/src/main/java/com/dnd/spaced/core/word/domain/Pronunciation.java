package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.exception.InvalidPronunciationContentException;
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

@Table(name = "pronunciations")
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pronunciation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    private String content;
    private PronunciationType type;

    public Pronunciation(String content, String typeName) {
        validateContent(content);

        this.content = content;
        this.type = PronunciationType.findBy(typeName);
    }

    public void initWord(Word word) {
        this.word = word;
    }

    private void validateContent(String content) {
        if (isInvalidContent(content)) {
            throw new InvalidPronunciationContentException("발음은 null이거나 비어 있을 수 없습니다.");
        }
    }

    private boolean isInvalidContent(String content) {
        return content == null || content.isBlank();
    }
}

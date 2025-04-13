package com.dnd.spaced.core.word.domain;

import com.dnd.spaced.core.word.domain.enums.PronunciationType;
import com.dnd.spaced.core.word.domain.exception.InvalidPronunciationContentException;
import com.dnd.spaced.global.audit.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private PronunciationType pronunciationType;

    private boolean deleted = false;

    public static Pronunciation of(String content, String typeName) {
        validateContent(content);

        return new Pronunciation(content, typeName);
    }

    private static void validateContent(String content) {
        if (isInvalidContent(content)) {
            throw new InvalidPronunciationContentException("발음은 null이거나 비어 있을 수 없습니다.");
        }
    }

    private static boolean isInvalidContent(String content) {
        return content == null || content.isBlank();
    }

    private Pronunciation(String content, String typeName) {
        this.content = content;
        this.pronunciationType = PronunciationType.findBy(typeName);
    }

    public void initWord(Word word) {
        this.word = word;
    }

    public void deleted() {
        this.deleted = true;
    }
}

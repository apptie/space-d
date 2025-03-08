package com.dnd.spaced.core.comment.domain;

import com.dnd.spaced.core.account.domain.Account;
import com.dnd.spaced.core.comment.domain.exception.InvalidCommentContentException;
import com.dnd.spaced.global.audit.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class Comment extends BaseTimeEntity {

    private static final int CONTENT_MIN_LENGTH = 1;
    private static final int CONTENT_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private Long wordId;

    private String content;

    private int likeCount = 0;

    private boolean isDeleted = false;

    public Comment(Long accountId, Long wordId, String content) {
        if (isInvalidContent(content)) {
            throw new InvalidCommentContentException("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
        }

        this.accountId = accountId;
        this.wordId = wordId;
        this.content = content;
    }

    private boolean isInvalidContent(String content) {
        return content == null || content.isBlank()
                || !(CONTENT_MIN_LENGTH <= content.length() && content.length() <= CONTENT_MAX_LENGTH);
    }

    public void delete() {
        isDeleted = true;
    }

    public void recover() {
        isDeleted = false;
    }

    public boolean isWriter(Long accountId) {
        return this.accountId.equals(accountId);
    }

    public boolean isWriter(Account account) {
        return account.isEqualTo(this.accountId);
    }

    public boolean isNotWriter(Account account) {
        return !isWriter(account);
    }

    public void changeContent(String content) {
        if (isInvalidContent(content)) {
            throw new InvalidCommentContentException("댓글 내용은 최소 1글자 이상, 최소 100글자 이하여야 합니다");
        }

        this.content = content;
    }
}

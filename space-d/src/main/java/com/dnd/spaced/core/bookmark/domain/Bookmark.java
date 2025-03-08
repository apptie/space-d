package com.dnd.spaced.core.bookmark.domain;

import com.dnd.spaced.global.audit.CreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "bookmarks")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class Bookmark extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private Long wordId;

    public Bookmark(Long accountId, Long wordId) {
        this.accountId = accountId;
        this.wordId = wordId;
    }

    public boolean isCreator(Long accountId) {
        return this.accountId.equals(accountId);
    }

    public boolean isNotCreator(Long accountId) {
        return !isCreator(accountId);
    }
}

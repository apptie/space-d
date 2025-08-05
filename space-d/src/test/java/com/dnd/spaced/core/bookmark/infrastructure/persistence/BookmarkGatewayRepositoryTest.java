package com.dnd.spaced.core.bookmark.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.bookmark.domain.Bookmark;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookmarkGatewayRepositoryTest {

    private static final long ACCOUNT_ID = 1L;
    private static final long WORD_ID = 1L;
    private static final long DELETED_WORD_ID = 2L;

    @Autowired
    BookmarkGatewayRepository bookmarkGatewayRepository;

    @Autowired
    EntityManager em;

    @Test
    @Sql("classpath:sql/bookmark/word.sql")
    void 북마크를_영속화_한다() {
        // given
        Bookmark bookmark = new Bookmark(ACCOUNT_ID, WORD_ID);

        // when
        bookmarkGatewayRepository.save(bookmark);

        // then
        Optional<Bookmark> actual = bookmarkGatewayRepository.findBy(ACCOUNT_ID, WORD_ID);

        assertThat(actual).isPresent();
    }

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 삭제하지_않은_용어에_등록된_북마크를_회원_id와_용어_id로_조회한다() {
        // when
        Optional<Bookmark> actual = bookmarkGatewayRepository.findBy(ACCOUNT_ID, WORD_ID);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getAccountId()).isEqualTo(ACCOUNT_ID),
                () -> assertThat(actual.get().getWordId()).isEqualTo(WORD_ID)
        );
    }

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 삭제한_용어에_등록된_북마크는_회원_id와_용어_id로_조회할_수_없다() {
        // when
        Optional<Bookmark> actual = bookmarkGatewayRepository.findBy(ACCOUNT_ID, DELETED_WORD_ID);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 삭제하지_않은_용어에_등록된_북마크를_회원_id와_용어_id로_영속화_여부를_확인한다() {
        // when
        boolean actual = bookmarkGatewayRepository.existsBy(ACCOUNT_ID, WORD_ID);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 삭제한_않은_용어에_등록된_북마크를_회원_id와_용어_id로_영속화_여부를_확인한다() {
        // when
        boolean actual = bookmarkGatewayRepository.existsBy(ACCOUNT_ID, DELETED_WORD_ID);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    @Transactional
    void 특정_용어_id에_등록된_모든_북마크를_삭제한다() {
        // when
        bookmarkGatewayRepository.deleteAllBy(Set.of(WORD_ID));

        // then
        boolean actual = bookmarkGatewayRepository.existsBy(ACCOUNT_ID, WORD_ID);

        assertThat(actual).isFalse();
    }

    @Test
    @Sql(value = {
            "classpath:sql/bookmark/word.sql",
            "classpath:sql/bookmark/bookmark.sql"
    })
    void 특정_회원이_삭제하지_않은_용어에_등록한_모든_북마크를_조회한다() {
        // when
        List<Bookmark> actual = bookmarkGatewayRepository.findAllBy(ACCOUNT_ID, null, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getAccountId()).isEqualTo(ACCOUNT_ID),
                () -> assertThat(actual.get(0).getWordId()).isEqualTo(WORD_ID)
        );
    }
}

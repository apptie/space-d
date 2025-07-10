package com.dnd.spaced.core.word.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.word.domain.dto.WordView;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchCondition;
import com.dnd.spaced.core.word.domain.repository.dto.request.WordSearchPageRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordViewGatewayRepositoryTest {

    private static final Long WORD_ID = 1L;
    private static final Long DELETED_WORD_ID = 2L;

    @Autowired
    WordViewGatewayRepository wordViewGatewayRepository;

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 삭제되지_않은_용어를_용어_id로_조회한다() {
        // when
        Optional<WordView> actual = wordViewGatewayRepository.findBy(WORD_ID);

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().id()).isEqualTo(WORD_ID)
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 삭제된_용어의_정보는_용어_id로_조회할_수_없다() {
        // when
        Optional<WordView> actual = wordViewGatewayRepository.findBy(DELETED_WORD_ID);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 삭제되지_않은_용어_목록을_조회한다() {
        // when
        List<WordView> actual = wordViewGatewayRepository.findAllBy(
                null,
                null,
                null,
                Pageable.ofSize(10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(10),
                () -> assertThat(convertWordId(actual)).doesNotContain(DELETED_WORD_ID)
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 삭제되지_않고_사전_순서로_debounce_이후_용어_목록을_조회한다() {
        // when
        List<WordView> actual = wordViewGatewayRepository.findAllBy(
                null,
                "debounce",
                Category.DEVELOP,
                Pageable.ofSize(10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(10),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("debounce")
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_삭제되지_않은_첫_번째_용어_목록을_조회한다() {
        // when
        List<WordView> actual = wordViewGatewayRepository.findAllBy(
                Category.DEVELOP,
                null,
                null,
                Pageable.ofSize(10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(10),
                () -> assertThat(convertWordId(actual)).doesNotContain(DELETED_WORD_ID)
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_사전_순서로_debounce_이후이면서_삭제되지_않은_용어_목록을_조회한다() {
        // when
        List<WordView> actual = wordViewGatewayRepository.findAllBy(
                Category.DEVELOP,
                "debounce",
                Category.DEVELOP,
                Pageable.ofSize(10)
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(10),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("debounce")
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition(null, Category.DEVELOP, null);
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(10), null, null);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(10),
                () -> assertThat(convertWordId(actual)).doesNotContain(DELETED_WORD_ID)
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_사전_순서로_용어_이름이_debounce_이후_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition(null, Category.DEVELOP, null);
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(10), "debounce", Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(10),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("debounce")
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_발음이_디로_시작하면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition(null, Category.DEVELOP, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), null, Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        assertThat(actual).hasSize(5);
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_발음이_디로_시작하면서_사전_순으로_용어_이름이_default_이후이면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition(null, Category.DEVELOP, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), "default", Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        assertAll(
                () -> assertThat(actual).hasSize(5),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("default")
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_발음이_디로_시작하면서_용어_이름이_de로_시작하면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition("de", Category.DEVELOP, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), null, Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertThat(actual).hasSize(5);
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 카테고리가_DEVELOP이면서_발음이_디로_시작하면서_용어_이름이_de로_시작하면서_사전_순으로_용어_이름이_design_이후이면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition("de", Category.DEVELOP, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), "design", Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("design")
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 발음이_디로_시작하면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition(null, null, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), null, null);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertThat(actual).hasSize(5);
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 발음이_디로_시작하면서_사전_순으로_용어_이름이_default_이후이면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition(null, null, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), "default", Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        assertAll(
                () -> assertThat(actual).hasSize(5),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("default")
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 발음이_디로_시작하면서_용어_이름이_de로_시작하면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition("de", null, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), null, Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertThat(actual).hasSize(5);
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 발음이_디로_시작하면서_용어_이름이_de로_시작하면서_사전_순으로_용어_이름이_design_이후이면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition("de", null, "디");
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), "design", Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("design")
        );
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 용어_이름이_de로_시작하면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition("de", null, null);
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), null, Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertThat(actual).hasSize(5);
    }

    @Test
    @Sql("classpath:sql/word/word_view.sql")
    void 용어_이름이_de로_시작하면서_사전_순으로_용어_이름이_design_이후이면서_삭제되지_않은_용어_목록을_검색한다() {
        // given
        WordSearchCondition condition = new WordSearchCondition("de", null, null);
        WordSearchPageRequest pageRequest = new WordSearchPageRequest(Pageable.ofSize(5), "design", Category.DEVELOP);

        // when
        List<WordView> actual = wordViewGatewayRepository.search(condition, pageRequest);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).name()).usingDefaultComparator().isGreaterThan("design")
        );
    }

    private List<Long> convertWordId(List<WordView> wordViews) {
        return wordViews.stream()
                        .map(WordView::id)
                        .toList();
    }
}

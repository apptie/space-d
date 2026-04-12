package com.dnd.spaced.core.admin.application.event.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.dnd.spaced.core.admin.application.AdminWordServiceFacade;
import com.dnd.spaced.core.admin.application.event.dto.DeletedWordEvent;
import com.dnd.spaced.core.word.application.repository.DeletedWordIdRepository;
import com.dnd.spaced.core.word.domain.repository.PronunciationRepository;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import com.dnd.spaced.core.word.domain.repository.WordRepository;
import com.dnd.spaced.fixture.LocalDateTimeFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminWordDeleteEventIntegrationTest {

    private static final Long WORD_ID = 1L;
    private static final long WORD_EXAMPLE_COUNT = 2L;
    private static final long PRONUNCIATION_COUNT = 2L;
    private static final LocalDateTime FAR_FUTURE = LocalDateTimeFixture.from("2999-12-31 23:59:59");

    @Autowired
    AdminWordServiceFacade adminWordServiceFacade;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    ApplicationEvents events;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    WordExampleRepository wordExampleRepository;

    @Autowired
    PronunciationRepository pronunciationRepository;

    @Autowired
    DeletedWordIdRepository deletedWordIdRepository;

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 외부_트랜잭션이_있다면_용어_삭제_이벤트를_처리하고_전체를_커밋한다() {
        // when
        transactionTemplate.executeWithoutResult(status -> adminWordServiceFacade.deleteWord(WORD_ID));

        // then
        assertAll(
                () -> assertThat(events.stream(DeletedWordEvent.class).count()).isOne(),
                () -> assertThat(wordRepository.existsBy(WORD_ID)).isFalse(),
                () -> assertThat(wordExampleRepository.countBy(WORD_ID)).isZero(),
                () -> assertThat(pronunciationRepository.countBy(WORD_ID)).isZero(),
                () -> assertThat(deletedWordIdRepository.findAllBy(FAR_FUTURE)).contains(WORD_ID)
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 외부_트랜잭션이_있고_용어_삭제_이벤트_처리중_예외가_발생하면_전체를_롤백한다() {
        // given
        doThrow(DataAccessResourceFailureException.class).when(deletedWordIdRepository)
                                                         .save(anyLong(), any(LocalDateTime.class));

        // when & then
        assertThatThrownBy(() -> transactionTemplate.executeWithoutResult(status -> adminWordServiceFacade.deleteWord(WORD_ID)))
                .isInstanceOf(DataAccessResourceFailureException.class);

        assertAll(
                () -> assertThat(wordRepository.existsBy(WORD_ID)).isTrue(),
                () -> assertThat(wordExampleRepository.countBy(WORD_ID)).isEqualTo(WORD_EXAMPLE_COUNT),
                () -> assertThat(pronunciationRepository.countBy(WORD_ID)).isEqualTo(PRONUNCIATION_COUNT),
                () -> assertThat(deletedWordIdRepository.findAllBy(FAR_FUTURE)).isEmpty(),
                () -> verify(deletedWordIdRepository).save(anyLong(), any(LocalDateTime.class))
        );
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/word/word_metadata.sql",
            "classpath:sql/admin/word/word.sql"
    })
    void 외부_트랜잭션이_없다면_용어_삭제_이벤트를_처리할_수_없다() {
        // when & then
        assertThatThrownBy(() -> applicationEventPublisher.publishEvent(new DeletedWordEvent(WORD_ID)))
                .isInstanceOf(IllegalTransactionStateException.class);

        assertAll(
                () -> assertThat(wordRepository.existsBy(WORD_ID)).isTrue(),
                () -> assertThat(wordExampleRepository.countBy(WORD_ID)).isEqualTo(WORD_EXAMPLE_COUNT),
                () -> assertThat(pronunciationRepository.countBy(WORD_ID)).isEqualTo(PRONUNCIATION_COUNT),
                () -> assertThat(deletedWordIdRepository.findAllBy(FAR_FUTURE)).isEmpty()
        );
    }
}

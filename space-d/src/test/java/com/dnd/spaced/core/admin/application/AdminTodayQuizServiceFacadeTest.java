package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.application.event.dto.AddedTodayQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.InvalidTodayQuizWordCountException;
import com.dnd.spaced.global.consts.CacheConst;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminTodayQuizServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    AdminTodayQuizServiceFacade adminTodayQuizServiceFacade;

    @Autowired
    CacheManager memoryCacheManager;

    @Test
    void 용어_메타데이터가_정상적으로_설정되지_않다면_오늘의_퀴즈를_생성할_수_없다() {
        // when & then
        assertThatThrownBy(() -> adminTodayQuizServiceFacade.createTodayQuiz())
                .isInstanceOf(WordMetadataNotFoundException.class)
                .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/quiz/word_metadata.sql",
            "classpath:sql/admin/quiz/quiz_metadata.sql"
    })
    void 등록된_용어_수가_퀴즈_생성_시_필요한_용어_수보다_적으면_퀴즈를_생성할_수_없다() {
        // when & then
        assertThatThrownBy(() -> adminTodayQuizServiceFacade.createTodayQuiz())
                .isInstanceOf(InvalidTodayQuizWordCountException.class)
                .hasMessage("오늘의 퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/quiz/word_metadata.sql",
            "classpath:sql/admin/quiz/quiz_metadata.sql",
            "classpath:sql/admin/quiz/word.sql"
    })
    void 오늘의_퀴즈를_생성한다() {
        // when
        Long savedTodayQuizId = adminTodayQuizServiceFacade.createTodayQuiz();

        // then
        assertAll(
                () -> assertThat(savedTodayQuizId).isPositive(),
                () -> assertThat(events.stream(AddedTodayQuizQuestionEvent.class).count()).isOne(),
                () -> assertThat(memoryCacheManager.getCache(CacheConst.TODAY_QUIZ_CACHE_NAME)).isNotNull()
        );
    }
}

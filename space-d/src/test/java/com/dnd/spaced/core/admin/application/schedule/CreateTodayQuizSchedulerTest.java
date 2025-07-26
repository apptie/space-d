package com.dnd.spaced.core.admin.application.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import com.dnd.spaced.core.quiz.domain.dto.SimpleTodayQuizDto;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateTodayQuizSchedulerTest {

    @Autowired
    CreateTodayQuizScheduler createTodayQuizScheduler;

    @Autowired
    TodayQuizRepository todayQuizRepository;

    @Autowired
    EntityManager em;

    @Test
    @Sql(scripts = {
            "classpath:sql/admin/quiz/word_metadata.sql",
            "classpath:sql/admin/quiz/quiz_metadata.sql",
            "classpath:sql/admin/quiz/word.sql"
    })
    void 오늘의_퀴즈를_생성한다() {
        // given
        Optional<SimpleTodayQuizDto> todayQuiz = todayQuizRepository.findLatest();

        assertThat(todayQuiz).isEmpty();

        // when
        createTodayQuizScheduler.schedule();

        // then
        em.clear();

        Optional<SimpleTodayQuizDto> actual = todayQuizRepository.findLatest();

        assertThat(actual).hasValueSatisfying(value -> {
            assertThat(value.id()).isEqualTo(1L);
        });
    }
}

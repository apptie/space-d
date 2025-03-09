package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.admin.application.helper.WithWordMetadataTestHelper;
import com.dnd.spaced.core.admin.application.helper.WithWordsTestHelper;
import com.dnd.spaced.core.quiz.application.event.dto.AddedTodayQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.InvalidTodayQuizWordCountException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanUpDatabase
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminTodayQuizServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    AdminTodayQuizService adminTodayQuizService;

    @Autowired
    TodayQuizRepository todayQuizRepository;

    @Nested
    class WithoutWordMetadataTest {

        @Test
        void 용어_메타데이터가_정상적으로_설정되지_않다면_오늘의_퀴즈를_생성할_수_없다() {
            // when & then
            assertThatThrownBy(() -> adminTodayQuizService.create())
                    .isInstanceOf(WordMetadataNotFoundException.class)
                    .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
        }
    }

    @Nested
    class WithWordMetadataTest extends WithWordMetadataTestHelper {

        @Test
        void 등록된_용어_수가_퀴즈_생성_시_필요한_용어_수보다_적으면_퀴즈를_생성할_수_없다() {
            // when & then
            assertThatThrownBy(() -> adminTodayQuizService.create())
                    .isInstanceOf(InvalidTodayQuizWordCountException.class)
                    .hasMessage("오늘의 퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
        }

        @Nested
        class WithWordsTest extends WithWordsTestHelper {

            @Test
            void 오늘의_퀴즈를_생성한다() {
                // when
                adminTodayQuizService.create();

                // then
                Optional<TodayQuiz> actual = todayQuizRepository.findBy(1L);

                assertAll(
                        () -> assertThat(actual).isPresent(),
                        () -> assertThat(actual.get().getId()).isEqualTo(1L),
                        () -> assertThat(actual.get().getTodayQuizOptions()).hasSize(4),
                        () -> assertThat(events.stream(AddedTodayQuizQuestionEvent.class).count()).isOne()
                );
            }
        }
    }
}

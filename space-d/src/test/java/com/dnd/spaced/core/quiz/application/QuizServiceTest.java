package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.event.dto.AddedQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.InvalidQuizWordCountException;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
import com.dnd.spaced.core.quiz.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.application.helper.WithWordMetadataTestHelper;
import com.dnd.spaced.core.quiz.application.helper.WithWordsTestHelper;
import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.GradedAnswerRepository;
import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@CleanUpDatabase
@RecordApplicationEvents
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuizServiceTest {

    @Autowired
    ApplicationEvents events;

    @Autowired
    QuizService quizService;

    @Autowired
    AdminWordService adminWordService;

    @Autowired
    GradedAnswerRepository gradedAnswerRepository;

    @Autowired
    WordMetadataRepository wordMetadataRepository;

    @Nested
    class WithoutMetadataTest {

        @Test
        void 용어_메타데이터가_정상적으로_설정되지_않다면_퀴즈를_생성할_수_없다() {
            // given
            CreateQuizRequest request = new CreateQuizRequest("전체 실무");

            // when & then
            assertThatThrownBy(() -> quizService.save(1L, request))
                    .isInstanceOf(WordMetadataNotFoundException.class)
                    .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
        }
    }

    @Nested
    class WithMetadataTest extends WithWordMetadataTestHelper {

        @Test
        void 등록된_용어_수가_퀴즈_생성_시_필요한_용어_수보다_적으면_퀴즈를_생성할_수_없다() {
            // given
            CreateQuizRequest request = new CreateQuizRequest("전체 실무");
            wordMetadataRepository.save(new WordMetadata());

            // when & then
            assertThatThrownBy(() -> quizService.save(1L, request))
                    .isInstanceOf(InvalidQuizWordCountException.class)
                    .hasMessage("퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
        }

        @Nested
        class WithWordsTest extends WithWordsTestHelper {

            @Test
            void 퀴즈를_생성한다() {
                // given
                CreateQuizRequest request = new CreateQuizRequest("전체 실무");

                // when
                Long actual = quizService.save(1L, request);

                // then
                assertAll(
                        () -> assertThat(actual).isPositive(),
                        () -> assertThat(events.stream(AddedQuizQuestionEvent.class).count()).isOne()
                );
            }

            @Test
            void 퀴즈를_조회한다() {
                // given
                CreateQuizRequest request = new CreateQuizRequest("전체 실무");
                Long quizId = quizService.save(1L, request);

                // when
                QuizResponse actual = quizService.findQuizBy(quizId);

                // then
                assertAll(
                        () -> assertThat(actual.id()).isEqualTo(quizId),
                        () -> assertThat(actual.accountId()).isEqualTo(1L),
                        () -> assertThat(actual.quizQuestions()).hasSize(5),
                        () -> assertThat(actual.quizQuestions().get(0).quizOptions()).hasSize(4),
                        () -> assertThat(actual.quizQuestions().get(1).quizOptions()).hasSize(4),
                        () -> assertThat(actual.quizQuestions().get(2).quizOptions()).hasSize(4),
                        () -> assertThat(actual.quizQuestions().get(3).quizOptions()).hasSize(4),
                        () -> assertThat(actual.quizQuestions().get(4).quizOptions()).hasSize(4)
                );
            }

            @Test
            void 유효하지_않는_퀴즈_id로_퀴즈를_조회할_수_없다() {
                // when & then
                assertThatThrownBy(() -> quizService.findQuizBy(-999L))
                        .isInstanceOf(QuizNotFoundException.class)
                        .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
            }

            @Test
            void 유효하지_않는_퀴즈_id로_퀴즈_답을_제출할_수_없다() {
                // given
                GradeQuizRequest request = new GradeQuizRequest(new int[]{0, 1, 2, 3, 2});

                // when & then
                assertThatThrownBy(() -> quizService.grade(1L, -999L, request))
                        .isInstanceOf(QuizNotFoundException.class)
                        .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
            }

            @Test
            void 퀴즈_정답을_제출한다() {
                // given
                CreateQuizRequest createQuizRequest = new CreateQuizRequest("전체 실무");
                Long quizId = quizService.save(1L, createQuizRequest);
                GradeQuizRequest gradeQuizRequest = new GradeQuizRequest(new int[]{0, 1, 2, 3, 2});

                // when
                quizService.grade(1L, quizId, gradeQuizRequest);

                // then
                List<GradedAnswer> actual = gradedAnswerRepository.findAllBy(quizId);

                assertAll(
                        () -> assertThat(actual).hasSize(5),
                        () -> assertThat(actual.get(0).getSelectedOptionIndex()).isEqualTo(0),
                        () -> assertThat(actual.get(1).getSelectedOptionIndex()).isEqualTo(1),
                        () -> assertThat(actual.get(2).getSelectedOptionIndex()).isEqualTo(2),
                        () -> assertThat(actual.get(3).getSelectedOptionIndex()).isEqualTo(3),
                        () -> assertThat(actual.get(4).getSelectedOptionIndex()).isEqualTo(2),
                        () -> assertThat(events.stream(GradedQuizEvent.class).count()).isOne()
                );
            }

            @Test
            void 모든_퀴즈의_제출했던_답을_조회한다() {
                // given
                Long quizId = quizService.save(1L, new CreateQuizRequest("전체 실무"));
                quizService.grade(1L, quizId, new GradeQuizRequest(new int[]{0, 1, 2, 3, 2}));

                // when
                GradedAnswerCollectionResponse actual = quizService.findGradedAnswersAllBy(
                        1L,
                        new ReadQuizGradedAnswerSearchRequest(null),
                        PageRequest.of(0, 10)
                );

                // then
                assertAll(
                        () -> assertThat(actual.answers()).hasSize(5),
                        () -> assertThat(actual.answers().get(0).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(1).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(2).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(3).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(4).selectedQuizOptionContent()).isNotBlank()
                );
            }

            @Test
            void 특정_퀴즈의_제출했던_답을_조회한다() {
                // given
                Long quizId = quizService.save(1L, new CreateQuizRequest("전체 실무"));
                GradeQuizRequest request = new GradeQuizRequest(new int[]{0, 1, 2, 3, 2});

                quizService.grade(1L, quizId, request);

                // when
                GradedAnswerCollectionResponse actual = quizService.findGradedAnswersAllBy(quizId);

                // then
                assertAll(
                        () -> assertThat(actual.answers()).hasSize(5),
                        () -> assertThat(actual.answers().get(0).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(1).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(2).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(3).selectedQuizOptionContent()).isNotBlank(),
                        () -> assertThat(actual.answers().get(4).selectedQuizOptionContent()).isNotBlank()
                );
            }
        }
    }
}

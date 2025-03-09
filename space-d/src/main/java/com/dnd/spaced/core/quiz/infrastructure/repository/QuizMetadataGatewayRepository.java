package com.dnd.spaced.core.quiz.infrastructure.repository;

import static com.dnd.spaced.core.quiz.domain.QQuizMetadata.quizMetadata;

import com.dnd.spaced.core.quiz.domain.QuizMetadata;
import com.dnd.spaced.core.quiz.domain.repository.QuizMetadataRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizMetadataGatewayRepository implements QuizMetadataRepository {

    private static final Long DEFAULT_QUIZ_METADATA_ID = 1L;
    private static final int QUIZ_QUESTION_COUNT = 5;
    private static final int TODAY_QUIZ_QUESTION_COUNT = 1;

    private final JPAQueryFactory queryFactory;
    private final QuizMetadataCrudRepository quizMetadataCrudRepository;

    @Override
    public void save(QuizMetadata quizMetadata) {
        quizMetadataCrudRepository.save(quizMetadata);
    }

    @Override
    public void updateQuizQuestionCount() {
        queryFactory.update(quizMetadata)
                            .set(
                                    quizMetadata.totalQuizQuestionCount,
                                    quizMetadata.totalQuizQuestionCount.add(QUIZ_QUESTION_COUNT)
                            )
                            .where(quizMetadata.id.eq(DEFAULT_QUIZ_METADATA_ID))
                            .execute();
    }

    @Override
    public void updateTodayQuizQuestionCount() {
        queryFactory.update(quizMetadata)
                    .set(
                            quizMetadata.totalTodayQuizQuestionCount,
                            quizMetadata.totalTodayQuizQuestionCount.add(TODAY_QUIZ_QUESTION_COUNT)
                    )
                    .where(quizMetadata.id.eq(DEFAULT_QUIZ_METADATA_ID))
                    .execute();
    }

    @Override
    public Optional<QuizMetadata> findBy(Long quizMetadataId) {
        return quizMetadataCrudRepository.findById(quizMetadataId);
    }
}

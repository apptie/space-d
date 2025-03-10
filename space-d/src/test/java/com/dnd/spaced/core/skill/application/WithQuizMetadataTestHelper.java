package com.dnd.spaced.core.skill.application;

import com.dnd.spaced.core.quiz.domain.QuizMetadata;
import com.dnd.spaced.core.quiz.domain.repository.QuizMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class WithQuizMetadataTestHelper {

    @Autowired
    QuizMetadataRepository quizMetadataRepository;

    @BeforeEach
    void beforeEach() {
        QuizMetadata quizMetadata = new QuizMetadata();
        ReflectionTestUtils.setField(quizMetadata, "totalQuizQuestionCount", 5L);
        quizMetadataRepository.save(quizMetadata);
    }
}

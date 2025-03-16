package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.WordRandom;
import java.util.List;

public interface WordRandomRepository {

    void saveWith(Long wordId, Category category);

    List<WordRandom> findAllBy(QuizCategory quizCategory, long limit);
}

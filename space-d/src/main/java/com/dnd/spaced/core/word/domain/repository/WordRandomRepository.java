package com.dnd.spaced.core.word.domain.repository;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.dto.SimpleWord;
import com.dnd.spaced.core.word.domain.enums.Category;
import java.util.List;

public interface WordRandomRepository {

    void saveWith(Word word, Category category);

    List<SimpleWord> findRandomAllBy(QuizCategory quizCategory, long limit);
}

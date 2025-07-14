package com.dnd.spaced.core.admin.application;

import com.dnd.spaced.core.admin.application.exception.WordExampleNotFoundException;
import com.dnd.spaced.core.word.domain.WordExample;
import com.dnd.spaced.core.word.domain.repository.WordExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UpdateWordService {

    private final WordExampleRepository wordExampleRepository;

    @Transactional
    public void updateWordExample(Long wordExampleId, String example) {
        WordExample wordExample = findWordExample(wordExampleId);

        executeWordExampleUpdate(example, wordExample);
    }

    private WordExample findWordExample(Long wordExampleId) {
        return wordExampleRepository.findBy(wordExampleId)
                                    .orElseThrow(() -> new WordExampleNotFoundException(
                                            "지정한 용어 예문을 찾을 수 없습니다.")
                                    );
    }

    private void executeWordExampleUpdate(String example, WordExample wordExample) {
        wordExample.changeExample(example);
    }
}

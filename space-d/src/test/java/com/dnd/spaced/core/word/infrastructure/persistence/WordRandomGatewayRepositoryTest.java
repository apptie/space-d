package com.dnd.spaced.core.word.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.word.domain.Word;
import com.dnd.spaced.core.word.domain.dto.SimpleWordInfo;
import com.dnd.spaced.core.word.domain.enums.Category;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordRandomGatewayRepositoryTest {

    @Autowired
    WordRandomCrudRepository wordRandomCrudRepository;

    @Autowired
    WordCrudRepository wordCrudRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    WordRandomGatewayRepository wordRandomGatewayRepository;

    @BeforeEach
    void beforeEach() {
        wordRandomGatewayRepository = new WordRandomGatewayRepository(wordRandomCrudRepository, jdbcTemplate);
    }

    @Test
    @Transactional
    void 용어_랜덤값을_저장한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();
        Word savedWord = wordCrudRepository.save(word);

        // when
        wordRandomGatewayRepository.saveWith(savedWord, Category.DEVELOP);

        // then
        List<SimpleWordInfo> actual = wordRandomGatewayRepository.findRandomAllBy(QuizCategory.DEVELOP, 1L);

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).id()).isPositive(),
                () -> assertThat(actual.get(0).meaning()).isEqualTo("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"),
                () -> assertThat(actual.get(0).name()).isEqualTo("Authorization")
        );
    }

    @Test
    void 동일한_카테고리인_용어_랜덤값을_전달한_limit만큼_조회한다() {
        // given
        Word authorizationWord = Word.builder()
                                     .name("Authorization")
                                     .meaning("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                                     .categoryName("개발")
                                     .build();
        Word yamlWord = Word.builder()
                            .name("YAML")
                            .meaning("사람이 읽기 쉬운 데이터 형식으로, 주로 설정 파일에 사용됩니다.")
                            .categoryName("개발")
                            .build();
        Word tomlWord = Word.builder()
                            .name("TOML")
                            .meaning("간단하고 가독성이 높은 설정 파일 형식으로, 키-값 쌍을 이용해 데이터를 표현합니다.")
                            .categoryName("개발")
                            .build();

        wordCrudRepository.saveAll(List.of(authorizationWord, yamlWord, tomlWord));
        wordRandomGatewayRepository.saveWith(authorizationWord, Category.DEVELOP);
        wordRandomGatewayRepository.saveWith(yamlWord, Category.DEVELOP);
        wordRandomGatewayRepository.saveWith(tomlWord, Category.DEVELOP);

        // when
        List<SimpleWordInfo> actual = wordRandomGatewayRepository.findRandomAllBy(QuizCategory.DEVELOP, 2L);

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 여러_카테고리인_용어_랜덤값을_전달한_limit만큼_조회한다() {
        // given
        Word authorizationWord = Word.builder()
                                     .name("Authorization")
                                     .meaning("인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                                     .categoryName("개발")
                                     .build();
        Word uxWord = Word.builder()
                          .name("UX")
                          .meaning("제품 사용 시 사용자가 느끼는 종합적인 경험을 연구하는 분야입니다.")
                          .categoryName("디자인")
                          .build();
        Word kpiWord = Word.builder()
                           .name("KPI")
                           .meaning("조직의 목표 달성 정도를 측정하는 핵심 기준입니다.")
                           .categoryName("비즈니스")
                           .build();

        wordCrudRepository.saveAll(List.of(authorizationWord, uxWord, kpiWord));
        wordRandomGatewayRepository.saveWith(authorizationWord, Category.DEVELOP);
        wordRandomGatewayRepository.saveWith(uxWord, Category.DESIGN);
        wordRandomGatewayRepository.saveWith(kpiWord, Category.BUSINESS);

        // when
        List<SimpleWordInfo> actual = wordRandomGatewayRepository.findRandomAllBy(QuizCategory.TOTAL, 3L);

        // then
        assertThat(actual).hasSize(3);
    }
}

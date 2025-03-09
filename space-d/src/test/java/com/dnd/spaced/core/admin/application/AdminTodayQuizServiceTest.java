package com.dnd.spaced.core.admin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto.PronunciationInfoDto;
import com.dnd.spaced.core.admin.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.application.event.dto.AddedTodayQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.InvalidTodayQuizWordCountException;
import com.dnd.spaced.core.quiz.domain.TodayQuiz;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.quiz.domain.repository.TodayQuizRepository;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
    AdminWordService adminWordService;

    @Autowired
    AdminTodayQuizService adminTodayQuizService;

    @Autowired
    WordMetadataRepository wordMetadataRepository;

    @Autowired
    TodayQuizRepository todayQuizRepository;

    @Test
    void 용어_메타데이터가_정상적으로_설정되지_않다면_오늘의_퀴즈를_생성할_수_없다() {
        // when & then
        assertThatThrownBy(() -> adminTodayQuizService.create())
                .isInstanceOf(WordMetadataNotFoundException.class)
                .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

    @Test
    void 등록된_용어_수가_퀴즈_생성_시_필요한_용어_수보다_적으면_퀴즈를_생성할_수_없다() {
        // given
        wordMetadataRepository.save(new WordMetadata());

        // when & then
        assertThatThrownBy(() -> adminTodayQuizService.create())
                .isInstanceOf(InvalidTodayQuizWordCountException.class)
                .hasMessage("오늘의 퀴즈를 진행할 수 있는 용어 개수가 부족합니다.");
    }

    @Test
    void 오늘의_퀴즈를_생성한다() {
        // given
        wordMetadataRepository.save(new WordMetadata());
        createWords();

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

    private void createWords() {
        createWord(
                "어써라이제이션",
                "게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.",
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘",
                QuizCategory.DEVELOP
        );
        createWord(
                "야믈",
                "YAML은 설정 파일이나 데이터 교환 포맷으로 자주 사용됩니다.",
                "YAML",
                "YAML은 사람이 읽기 쉬운 데이터 형식으로, 주로 설정 파일에 사용됩니다.",
                QuizCategory.DEVELOP
        );
        createWord(
                "톰엘",
                "TOML은 구성 파일에 사용하기 쉬운 데이터 직렬화 언어입니다.",
                "TOML",
                "TOML은 간단하고 가독성이 높은 설정 파일 형식으로, 키-값 쌍을 이용해 데이터를 표현합니다.",
                QuizCategory.DEVELOP
        );
        createWord(
                "데프리케이티드",
                "이 함수는 더 이상 사용되지 않으므로 deprecated되었습니다.",
                "deprecated",
                "deprecated는 더 이상 사용되지 않거나, 지원되지 않는다는 뜻입니다.",
                QuizCategory.DEVELOP
        );
        createWord(
                "아르오아이",
                "투자 대비 얻은 수익의 비율을 계산하는 지표입니다.",
                "ROI",
                "신제품 출시 ROI 분석 결과 150%의 효율성이 확인되었습니다.",
                QuizCategory.BUSINESS
        );
        createWord(
                "번 레이트",
                "스타트업이 매월 소비하는 현금의 양을 의미합니다.",
                "Burn Rate",
                "현재 번 레이트가 월 5천만 원이라면 6개월 후 자본 고갈 위헩니다.",
                QuizCategory.BUSINESS
        );
        createWord(
                "케이피아이",
                "조직의 목표 달성 정도를 측정하는 핵심 기준입니다.",
                "KPI",
                "이번 분기 KPI로 고객 유지율 85%를 설정했습니다.",
                QuizCategory.BUSINESS
        );
        createWord(
                "스크럼",
                "애자일 개발 방법론 중 반복적인 프로젝트 관리 기법입니다.",
                "Scrum",
                "매일 15분 스크럼 미팅으로 작업 현황을 공유합니다.",
                QuizCategory.BUSINESS
        );
        createWord(
                "반응형 디자인",
                "화면 크기에 따라 레이아웃이 자동으로 조절되는 디자인 기법입니다.",
                "Responsive Design",
                "모바일과 PC에서 모두 최적화된 반응형 디자인을 적용했습니다.",
                QuizCategory.DESIGN
        );
        createWord(
                "유저 익스피리언스",
                "제품 사용 시 사용자가 느끼는 종합적인 경험을 연구하는 분야입니다.",
                "UX",
                "UX 개선을 위해 사용자 행동 패턴을 분석 중입니다.",
                QuizCategory.DESIGN
        );
        createWord(
                "에이비 테스팅",
                "두 가지 디자인 버전을 비교해 효과를 측정하는 실험 방법입니다.",
                "A/B Testing",
                "버튼 색상 변경 A/B 테스트에서 빨간색이 30% 더 클릭되었습니다.",
                QuizCategory.DESIGN
        );
        createWord(
                "그리드 시스템",
                "레이아웃을 구조화하기 위해 열과 행을 기준으로 요소를 배치하는 방식입니다.",
                "Grid System",
                "그리드 시스템을 사용해 콘텐츠의 시각적 균형을 맞췄습니다.",
                QuizCategory.DESIGN
        );
    }

    private void createWord(
            String pronunciationKorean,
            String exampleContent,
            String wordName,
            String wordMeaning,
            QuizCategory quizCategory
    ) {
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(
                new PronunciationInfoDto(pronunciationKorean, "한글 발음")
        );
        List<String> examples = List.of(exampleContent);
        SaveWordDto saveWordDto = new SaveWordDto(wordName, wordMeaning, quizCategory.getName(), pronunciationInfoDtos, examples);

        adminWordService.saveWord(saveWordDto);
    }
}

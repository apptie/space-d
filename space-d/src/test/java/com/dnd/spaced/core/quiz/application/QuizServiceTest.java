package com.dnd.spaced.core.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.dnd.spaced.config.clean.annotation.CleanUpDatabase;
import com.dnd.spaced.core.admin.application.AdminWordService;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto;
import com.dnd.spaced.core.admin.application.dto.request.SaveWordDto.PronunciationInfoDto;
import com.dnd.spaced.core.quiz.application.dto.request.CreateQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.GradeQuizRequest;
import com.dnd.spaced.core.quiz.application.dto.request.ReadQuizGradedAnswerSearchRequest;
import com.dnd.spaced.core.quiz.application.dto.response.GradedAnswerCollectionResponse;
import com.dnd.spaced.core.quiz.application.dto.response.QuizResponse;
import com.dnd.spaced.core.quiz.application.event.dto.AddedQuizQuestionEvent;
import com.dnd.spaced.core.quiz.application.exception.InvalidQuizWordCountException;
import com.dnd.spaced.core.quiz.application.exception.QuizNotFoundException;
import com.dnd.spaced.core.quiz.application.exception.WordMetadataNotFoundException;
import com.dnd.spaced.core.quiz.domain.GradedAnswer;
import com.dnd.spaced.core.quiz.domain.repository.GradedAnswerRepository;
import com.dnd.spaced.core.skill.application.event.dto.GradedQuizEvent;
import com.dnd.spaced.core.word.domain.WordMetadata;
import com.dnd.spaced.core.word.domain.repository.WordMetadataRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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

    @Test
    void 용어_메타데이터가_정상적으로_설정되지_않다면_퀴즈를_생성할_수_없다() {
        // given
        CreateQuizRequest request = new CreateQuizRequest("전체 실무");

        // when & then
        assertThatThrownBy(() -> quizService.save(1L, request))
                .isInstanceOf(WordMetadataNotFoundException.class)
                .hasMessage("용어 메타데이터가 정상적으로 설정되지 않았습니다.");
    }

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

    @Test
    void 퀴즈를_생성한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();
        wordMetadataRepository.save(wordMetadata);
        createWords();
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
    void 존재하지_않는_퀴즈_id로_퀴즈를_조회할_수_없다() {
        // when & then
        assertThatThrownBy(() -> quizService.findQuizBy(1L))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    void 퀴즈를_조회한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();
        wordMetadataRepository.save(wordMetadata);
        createWords();
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
    void 퀴즈_정답을_제출한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();
        wordMetadataRepository.save(wordMetadata);
        createWords();
        Long quizId = quizService.save(1L, new CreateQuizRequest("전체 실무"));

        // when
        quizService.grade(1L, quizId, new GradeQuizRequest(new int[]{0, 1, 2, 3, 2}));

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
    void 퀴즈_id가_유효하지_않다면_퀴즈_답을_제출할_수_없다() {
        // given
        GradeQuizRequest request = new GradeQuizRequest(new int[]{0, 1, 2, 3, 2});

        // when & then
        assertThatThrownBy(() -> quizService.grade(1L, 1L, request))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessage("지정한 id의 퀴즈를 찾지 못했습니다.");
    }

    @Test
    void 모든_퀴즈의_제출했던_답을_조회한다() {
        // given
        WordMetadata wordMetadata = new WordMetadata();
        wordMetadataRepository.save(wordMetadata);
        createWords();
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
        WordMetadata wordMetadata = new WordMetadata();
        wordMetadataRepository.save(wordMetadata);
        createWords();
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

    private void createWords() {
        createWord(
                "어써라이제이션",
                "게시글 삭제는 작성자와 관리자만 Authorization이 있도록 구현했습니다.",
                "Authorization",
                "Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘"
        );
        createWord(
                "야믈",
                "YAML은 설정 파일이나 데이터 교환 포맷으로 자주 사용됩니다.",
                "YAML",
                "YAML은 사람이 읽기 쉬운 데이터 형식으로, 주로 설정 파일에 사용됩니다."
        );
        createWord(
                "톰엘",
                "TOML은 구성 파일에 사용하기 쉬운 데이터 직렬화 언어입니다.",
                "TOML",
                "TOML은 간단하고 가독성이 높은 설정 파일 형식으로, 키-값 쌍을 이용해 데이터를 표현합니다."
        );
        createWord(
                "데프리케이티드",
                "이 함수는 더 이상 사용되지 않으므로 deprecated되었습니다.",
                "deprecated",
                "deprecated는 더 이상 사용되지 않거나, 지원되지 않는다는 뜻입니다."
        );
        createWord(
                "엑시큐트",
                "코드를 실행하려면 ‘Run’ 버튼을 눌러서 execute시킵니다.",
                "execute",
                "개발에서는 주로 프로그램이나 코드, 명령을 실행할 때 사용됩니다."
        );
        createWord(
                "코얼레스",
                "데이터베이스 쿼리에서 COALESCE 함수를 사용해 NULL 값을 빈 문자열로 대체합니다.",
                "COALESCE",
                "SQL에서 인자로 주어진 컬럼들 중에서 NULL이 아닌 첫 번째 값을 반환하는 함수입니다."
        );
        createWord(
                "큐",
                "비동기 작업을 처리하기 위해 작업 Queue를 사용하여 작업을 순차적으로 실행합니다.",
                "Queue",
                "대기열을 의미하며, 데이터 구조에서 먼저 들어온 데이터가 먼저 나가는(FIFO) 방식의 대기열을 의미합니다."
        );
        createWord(
                "캐러셀",
                "사용자는 이미지 캐러셀을 통해 다양한 사진을 볼 수 있습니다.",
                "carousel",
                "회전목마를 의미하며, UI 중 이미지를 순환하며 보여주는 슬라이더를 지칭합니다."
        );
        createWord(
                "디큐",
                "사용자 요청을 Queue에 저장하고, 순서대로 Dequeue하여 처리합니다.",
                "Dequeue",
                "Queue의 반대 동작으로, 큐에 저장된 데이터 중 첫 번째 요소를 제거하고 반환하는 것을 의미합니다."
        );
        createWord(
                "제이더블유티",
                "사용자가 로그인하면 서버는 JWT를 생성하여 클라이언트에게 반환합니다.",
                "JWT",
                "JWT는 JSON Web Token의 약자로, JSON 형식의 웹 토큰을 의미합니다."
        );
        createWord(
                "구이",
                "새로 출시된 운영 체제는 직관적인 GUI를 제공하여 사용자가 쉽게 파일을 관리하고 프로그램을 실행할 수 있습니다.",
                "GUI",
                "그래픽 사용자 인터페이스를 의미합니다."
        );
        createWord(
                "유씨지",
                "시스템의 CPU usage가 80%를 초과했습니다.",
                "usage",
                "사용, 용법을 뜻합니다. CPU, 메모리, 네트워크 등 시스템이나 소프트웨어 자원의 사용량을 의미합니다."
        );
        createWord(
                "사스",
                "회사에 클라우드 기반 SaaS 솔루션을 도입했습니다.",
                "SaaS",
                "Software as a Service의 약자로, 서비스형 소프트웨어를 의미합니다."
        );
        createWord(
                "디렉터리",
                "directory 권한 설정을 통해 특정 사용자만 접근할 수 있도록 했습니다.",
                "directory",
                "파일 시스템에서 파일과 폴더를 계층적으로 구성하는 데 사용되는 구조를 의미합니다."
        );
        createWord(
                "엠티",
                "배열이 empty인지 확인한 후에 데이터 추가 작업을 수행합니다.",
                "empty",
                "비어 있는, 아무것도 없는 상태를 의미하며, 변수나 데이터 구조가 값을 포함하지 않은 상태를 의미합니다."
        );
        createWord(
                "리다이렉트",
                "HTTP 상태 코드 중 301은 영구적으로 다른 URL로 redirect하며, 302는 일시적으로 다른 URL로 redirect했다는 뜻입니다.",
                "redirect",
                "웹 서버나 애플리케이션에서 사용자가 요청한 URL을 다른 URL로 자동으로 보내는 행위를 의미합니다."
        );
        createWord(
                "자르",
                "JAR 파일을 실행하여 애플리케이션을 시작합니다",
                "jar",
                "자바 애플리케이션을 패키징하여 배포하는 데 사용되는 파일 형식으로, 여러 클래스 파일과 관련 메타데이터를 포함합니다."
        );
        createWord(
                "로캘",
                "시스템의 locale을 한국어로 설정합니다.",
                "locale",
                "소프트웨어나 시스템에서 특정 언어와 문화권에 맞춘 설정을 의미합니다."
        );
        createWord(
                "그레이디언트",
                "CSS에서 linear-gradient와 radial-gradient 속성을 사용하여 다양한 형태의 gradient를 만들 수 있습니다.",
                "gradient",
                "CSS에서 색상이나 밝기가 점진적으로 변하는 효과로, 웹 페이지의 배경이나 요소에 주로 적용합니다."
        );
        createWord(
                "스테이터스",
                "클라이언트의 요청이 성공적으로 처리되었음을 나타내기 위해 서버는 200 OK HTTP status 코드를 반환합니다.",
                "status",
                "시스템, 프로세스, 작업, 또는 소프트웨어의 현재 상태나 진행 상황을 의미합니다."
        );
    }

    private void createWord(String pronunciationKorean, String exampleContent, String wordName, String wordMeaning) {
        List<PronunciationInfoDto> pronunciationInfoDtos = List.of(
                new PronunciationInfoDto(pronunciationKorean, "한글 발음")
        );
        List<String> examples = List.of(exampleContent);
        SaveWordDto saveWordDto = new SaveWordDto(wordName, wordMeaning, "개발", pronunciationInfoDtos, examples);

        adminWordService.saveWord(saveWordDto);
    }
}

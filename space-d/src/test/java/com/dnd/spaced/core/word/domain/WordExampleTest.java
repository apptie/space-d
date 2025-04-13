package com.dnd.spaced.core.word.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.word.domain.exception.InvalidWordExampleContentException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WordExampleTest {

    @Test
    void 용어_예문을_초기화한다() {
        // when
        String example = "시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다.";
        WordExample wordExample = WordExample.from(example);

        // then
        assertThat(wordExample.getExample()).isEqualTo(example);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 글자_수가_유효하지_않은_용어_예문_내용을_전달하면_용어_예문을_초기화_할_수_없다(String invalidExample) {
        // when & then
        assertThatThrownBy(() -> WordExample.from(invalidExample))
                .isInstanceOf(InvalidWordExampleContentException.class)
                .hasMessage("예문의 길이는 최소 1글자 이상, 최대 150글자 이하여야 합니다.");
    }

    @Test
    void 용어_예문에_용어_정보를_추가한다() {
        // given
        Word word = Word.builder()
                        .name("Authorization")
                        .meaning("Authorization(권한 부여)은 인증된 사용자가 특정 리소스나 기능에 접근할 수 있는 권한이 있는지를 확인하고 제어하는 보안 메커니즘")
                        .categoryName("개발")
                        .build();
        WordExample wordExample = WordExample.from("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다.");

        // when
        wordExample.initWord(word);

        // then
        assertThat(wordExample.getWord()).isNotNull();
    }

    @Test
    void 용어_예문을_변경한다() {
        // given
        WordExample wordExample = WordExample.from("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다.");
        String changedExample = "보안 팀장은 외부 감사관의 데이터베이스 접근 Authorization을 일시적으로 승인했다.";

        // when
        wordExample.changeExample(changedExample);

        // then
        assertThat(wordExample.getExample()).isEqualTo(changedExample);
    }

    @Test
    void 용어_예문의_식별자_여부를_판단한다() {
        // given
        WordExample wordExample = WordExample.from("시스템 관리자는 신입 직원들에게 회사 내부 네트워크에 대한 Authorization을 부여했다.");
        ReflectionTestUtils.setField(wordExample, "id", 1L);

        // when
        boolean actual = wordExample.isEqualTo(1L);

        // then
        assertThat(actual).isTrue();
    }
}

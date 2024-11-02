package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidRoleNameException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RoleTest {

    private static Stream<Arguments> findByTestArguments() {
        return Stream.of(
                Arguments.of("ROLE_ADMIN", Role.ROLE_ADMIN),
                Arguments.of("ROLE_USER", Role.ROLE_USER)
        );
    }

    @ParameterizedTest(name = "권한 정보 이름이 {0}일 때 {1}을 반환한다")
    @MethodSource("findByTestArguments")
    void 권한_정보를_찾는다(String roleName, Role expected) {
        // when
        Role actual = Role.findBy(roleName);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "권한 정보 이름이 {0}일 때 예외가 발생한다")
    @NullAndEmptySource
    void 권한_정보를_찾을_때_유효한_권한_정보_이름이_아니라면_권한_정보를_찾을_수_없다(String invalidRoleName) {
        // when & then
        assertThatThrownBy(() -> Role.findBy(invalidRoleName))
                .isInstanceOf(InvalidRoleNameException.class)
                .hasMessageContaining("잘못된 권한 정보 이름");
    }
}

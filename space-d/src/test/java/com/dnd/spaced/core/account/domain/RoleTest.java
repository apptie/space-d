package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidRoleNameException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RoleTest {

    @Test
    void findBy_메서드는_유효한_roleName을_전달하면_roleName에_맞는_Role을_반환한다() {
        // given
        Role admin = Role.ROLE_ADMIN;

        // when
        Role actual = Role.findBy(admin.name());

        // then
        assertThat(actual).isEqualTo(admin);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findBy_메서드는_유효하지_않은_roleName을_전달하면_InvalidRoleNameException_예외가_발생한다(String invalidRoleName) {
        // when & then
        assertThatThrownBy(() -> Role.findBy(invalidRoleName))
                .isInstanceOf(InvalidRoleNameException.class)
                .hasMessageContaining("잘못된 role name");
    }
}

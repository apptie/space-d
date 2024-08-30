package com.dnd.spaced.core.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dnd.spaced.core.account.domain.exception.InvalidCompanyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CompanyTest {

    @Test
    void findBy_메서드는_유효한_name을_전달하면_name에_맞는_Company를_반환한다() {
        // given
        Company blind = Company.BLIND;

        // when
        Company actual = Company.findBy(blind.getName());

        // then
        assertThat(actual).isEqualTo(blind);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findBy_메서드는_유효하지_않은_name을_전달하면_InvalidCompanyException_예외가_발생한다(String invalidName) {
        // when & then
        assertThatThrownBy(() -> Company.findBy(invalidName))
                .isInstanceOf(InvalidCompanyException.class)
                .hasMessageContaining("잘못된 회사 이름");
    }
}

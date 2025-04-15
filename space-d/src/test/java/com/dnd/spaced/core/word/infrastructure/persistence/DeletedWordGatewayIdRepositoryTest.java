package com.dnd.spaced.core.word.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.dnd.spaced.fixture.LocalDateTimeFixture;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeletedWordGatewayIdRepositoryTest {

    @Autowired
    DeletedWordGatewayIdRepository deletedWordGatewayIdRepository;

    @Test
    void 용어_삭제_이벤트를_등록한다() {
        // when & then
        assertDoesNotThrow(() ->
                deletedWordGatewayIdRepository.save(
                        1L,
                        LocalDateTimeFixture.from("2022-02-02 13:13:00")
                )
        );
    }

    @Test
    void 특정_시간_이전에_등록한_용어_삭제_이벤트를_조회한다() {
        // given
        deletedWordGatewayIdRepository.save(1L, LocalDateTimeFixture.from("2022-02-02 13:13:00"));
        deletedWordGatewayIdRepository.save(2L, LocalDateTimeFixture.from("2022-02-03 13:13:00"));
        deletedWordGatewayIdRepository.save(3L, LocalDateTimeFixture.from("2022-02-04 13:13:00"));

        // when
        Set<Long> actual = deletedWordGatewayIdRepository.findAllBy(
                LocalDateTimeFixture.from("2022-02-03 15:13:00")
        );

        // then
        assertThat(actual).hasSize(2)
                          .contains(1L)
                          .contains(2L);
    }

    @Test
    void 특정_시간_이전에_등록한_용어_삭제_이벤트를_삭제한다() {
        // given
        deletedWordGatewayIdRepository.save(1L, LocalDateTimeFixture.from("2022-02-02 13:13:00"));
        deletedWordGatewayIdRepository.save(2L, LocalDateTimeFixture.from("2022-02-03 13:13:00"));
        deletedWordGatewayIdRepository.save(3L, LocalDateTimeFixture.from("2022-02-04 13:13:00"));

        // when
        deletedWordGatewayIdRepository.deleteAllBy(
                LocalDateTimeFixture.from("2022-02-03 15:13:00")
        );

        // then
        Set<Long> actual = deletedWordGatewayIdRepository.findAllBy(
                LocalDateTimeFixture.from("2022-02-05 15:13:00")
        );

        assertThat(actual).hasSize(1)
                          .contains(3L);
    }
}

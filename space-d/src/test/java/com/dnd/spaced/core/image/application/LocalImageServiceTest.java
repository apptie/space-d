package com.dnd.spaced.core.image.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LocalImageServiceTest {

    @Autowired
    LocalImageService localImageService;

    @Test
    void 로컬에_저장된_이미지를_조회한다() {
        // when
        Resource actual = localImageService.readImage("earth.png");

        // then
        assertAll(
                () -> assertThat(actual.getFilename()).isEqualTo("earth.png"),
                () -> assertThat(actual.getURL().toString()).isEqualTo("file:/home/earth.png"),
                () -> assertThat(actual.getFile().getAbsolutePath()).isEqualTo("/home/earth.png")
        );
    }
}

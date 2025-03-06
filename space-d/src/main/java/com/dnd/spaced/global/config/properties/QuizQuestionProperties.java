package com.dnd.spaced.global.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("app.quiz")
@RequiredArgsConstructor
public class QuizQuestionProperties {

    private final String question;
}


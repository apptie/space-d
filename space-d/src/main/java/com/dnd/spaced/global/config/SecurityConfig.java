package com.dnd.spaced.global.config;

import com.dnd.spaced.global.config.properties.TokenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {TokenProperties.class})
public class SecurityConfig {
}

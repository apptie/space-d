package com.dnd.spaced.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auth")
public record AesProperties(String secretKey, String salt) {

}

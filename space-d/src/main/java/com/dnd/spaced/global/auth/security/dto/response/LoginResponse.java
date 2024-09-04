package com.dnd.spaced.global.auth.security.dto.response;

public record LoginResponse(String accessToken, String tokenScheme, boolean isSignUp) {
}

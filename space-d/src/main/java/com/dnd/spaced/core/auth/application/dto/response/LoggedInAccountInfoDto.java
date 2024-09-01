package com.dnd.spaced.core.auth.application.dto.response;

public record LoggedInAccountInfoDto(String id, String roleName, boolean isSignUp) {
}

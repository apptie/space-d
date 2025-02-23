package com.dnd.spaced.core.auth.application.dto.response;

public record LoggedInAccountInfoDto(Long id, String roleName, boolean isSignUp) {
}

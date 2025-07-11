package com.dnd.spaced.core.auth.application.dto.response;

public record LoggedInAccountDto(Long id, String roleName, boolean isSignUp) {
}

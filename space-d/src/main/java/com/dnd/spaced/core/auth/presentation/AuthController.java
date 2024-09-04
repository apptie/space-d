package com.dnd.spaced.core.auth.presentation;

import com.dnd.spaced.core.auth.application.AuthService;
import com.dnd.spaced.core.auth.application.InitAccountInfoService;
import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.core.auth.presentation.dto.request.UpdateAccountCareerInfoRequest;
import com.dnd.spaced.core.auth.presentation.dto.response.AccessTokenResponse;
import com.dnd.spaced.core.auth.presentation.exception.RefreshTokenNotFoundException;
import com.dnd.spaced.global.auth.AuthAccount;
import com.dnd.spaced.global.auth.AuthAccountInfo;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.dnd.spaced.global.consts.controller.ResponseEntityConst;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_KEY = "refreshToken";
    private static final String COOKIE_DOMAIN = "/";

    private final TokenProperties tokenProperties;
    private final AuthService authService;
    private final InitAccountInfoService initAccountInfoService;

    @PostMapping("/profile")
    public ResponseEntity<Void> initAccountProfile(
            @AuthAccount AuthAccountInfo accountInfo,
            @Valid @RequestBody UpdateAccountCareerInfoRequest request
    ) {
        initAccountInfoService.initCareerInfo(
                accountInfo.id(),
                request.jobGroupName(),
                request.companyName(),
                request.experienceName()
        );

        return ResponseEntityConst.NO_CONTENT;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = findRefreshToken(request.getCookies()).orElseThrow(
                () -> new RefreshTokenNotFoundException("Cookie에서 refreshToken을 찾을 수 없습니다.")
        );

        TokenDto tokenDto = authService.refreshToken(refreshToken);
        HttpCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_KEY, tokenDto.refreshToken())
                                          .httpOnly(true)
                                          .secure(true)
                                          .sameSite(SameSite.NONE.name())
                                          .maxAge(tokenProperties.refreshExpiredSeconds())
                                          .path(COOKIE_DOMAIN)
                                          .build();

        return ResponseEntity.ok()
                             .header(HttpHeaders.SET_COOKIE, cookie.toString())
                             .body(new AccessTokenResponse(tokenDto.accessToken(), tokenDto.tokenScheme()));
    }

    private Optional<String> findRefreshToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (REFRESH_TOKEN_COOKIE_KEY.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }
}

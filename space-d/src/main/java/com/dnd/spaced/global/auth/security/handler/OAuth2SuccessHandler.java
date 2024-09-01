package com.dnd.spaced.global.auth.security.handler;

import com.dnd.spaced.core.auth.application.GenerateTokenService;
import com.dnd.spaced.core.auth.application.LoginService;
import com.dnd.spaced.core.auth.application.dto.response.LoggedInAccountInfoDto;
import com.dnd.spaced.core.auth.application.dto.response.TokenDto;
import com.dnd.spaced.global.auth.exception.InvalidResponseWriteException;
import com.dnd.spaced.global.auth.security.dto.response.LoginResponse;
import com.dnd.spaced.global.config.properties.TokenProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    public static final String DOMAIN = "/";
    public static final String REFRESH_TOKEN_KEY = "refreshToken";

    private final ObjectMapper objectMapper;
    private final TokenProperties tokenProperties;
    private final LoginService loginService;
    private final GenerateTokenService generateTokenService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String id = (String) oAuth2User.getAttributes()
                                       .get(StandardClaimNames.SUB);
        LoggedInAccountInfoDto accountInfoDto = loginService.login(id);
        TokenDto tokenDto = generateTokenService.generate(accountInfoDto.id(), accountInfoDto.roleName());

        writeResponse(response, tokenDto.accessToken(), accountInfoDto.isSignUp());
        createRefreshTokenCookie(response, tokenDto.refreshToken());
    }

    private void writeResponse(HttpServletResponse response, String accessToken, boolean isSignUp) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            PrintWriter writer = response.getWriter();

            writer.println(objectMapper.writeValueAsString(new LoginResponse(accessToken, isSignUp)));
            writer.flush();
        } catch (IOException e) {
            throw new InvalidResponseWriteException();
        }
    }

    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_KEY, URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));

        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(tokenProperties.refreshExpiredSeconds());
        cookie.setPath(DOMAIN);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }
}

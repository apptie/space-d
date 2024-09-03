package com.dnd.spaced.global.auth.interceptor;

import com.dnd.spaced.global.auth.AuthAccountInfo;
import com.dnd.spaced.global.auth.AuthStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthStore store;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            store.set(new AuthAccountInfo(null));
            return true;
        }

        String id = ((UserDetails) authentication.getPrincipal()).getUsername();

        store.set(new AuthAccountInfo(id));
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest ignoreRequest,
            HttpServletResponse ignoreResponse,
            Object ignoreHandler,
            Exception ignoreEx
    ) {
        store.remove();
    }
}

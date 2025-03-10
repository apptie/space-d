package com.dnd.spaced.global.auth.resolver;

import com.dnd.spaced.global.auth.AccountInfo;
import com.dnd.spaced.global.auth.AuthStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class GuestAccountInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthStore store;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentAccountInfo.class) && parameter.getParameterType()
                                                                                      .equals(GuestAccountInfo.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        AccountInfo accountInfo = store.get();

        if (isInvalidAccountPrincipal(accountInfo)) {
            return new GuestAccountInfo();
        }

        return new GuestAccountInfo(accountInfo.accountId());
    }

    private boolean isInvalidAccountPrincipal(AccountInfo accountInfo) {
        return accountInfo == null || accountInfo.accountId() == null;
    }
}

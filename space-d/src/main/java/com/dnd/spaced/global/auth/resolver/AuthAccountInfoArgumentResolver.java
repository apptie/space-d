package com.dnd.spaced.global.auth.resolver;

import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.global.auth.AccountInfo;
import com.dnd.spaced.global.auth.AuthStore;
import com.dnd.spaced.global.auth.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthAccountInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthStore store;
    private final AccountRepository accountRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentAccountInfo.class) && parameter.getParameterType()
                                                                                      .equals(AuthAccountInfo.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        AccountInfo accountInfo = store.get();

        validateAccountPrincipal(accountInfo);

        Long accountId = accountInfo.accountId();

        validateExistsAccountId(accountId);

        return new AuthAccountInfo(accountInfo.accountId());
    }

    private void validateAccountPrincipal(AccountInfo accountInfo) {
        if (accountInfo == null || accountInfo.accountId() == null) {
            throw new UnauthorizedException();
        }
    }

    private void validateExistsAccountId(Long accountId) {
        if (!accountRepository.existsBy(accountId)) {
            throw new UnauthorizedException();
        }
    }
}

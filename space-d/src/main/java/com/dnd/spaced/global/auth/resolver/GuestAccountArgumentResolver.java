package com.dnd.spaced.global.auth.resolver;

import com.dnd.spaced.core.account.domain.repository.AccountRepository;
import com.dnd.spaced.global.auth.AccountId;
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
public class GuestAccountArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthStore store;
    private final AccountRepository accountRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentAccount.class) && parameter.getParameterType()
                                                                                  .equals(GuestAccountId.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        AccountId accountId = store.get();

        if (isEmptyAccountPrincipal(accountId)) {
            return new GuestAccountId();
        }

        validateExistsAccountId(accountId.id());

        return new GuestAccountId(accountId.id());
    }

    private boolean isEmptyAccountPrincipal(AccountId accountId) {
        return accountId == null || accountId.id() == null;
    }

    private void validateExistsAccountId(Long accountId) {
        if (isBrokenAccount(accountId)) {
            throw new UnauthorizedException();
        }
    }

    private boolean isBrokenAccount(Long accountId) {
        return !accountRepository.existsBy(accountId);
    }
}

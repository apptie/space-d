package com.dnd.spaced.global.resolver.word;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CommonWordPageableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int IGNORED_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_CONDITION = "name";
    private static final Direction DEFAULT_DIRECTION = Direction.ASC;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CommonWordPageable.class) && parameter.getParameterType()
                                                                                      .equals(Pageable.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        return PageRequest.of(IGNORED_PAGE, DEFAULT_SIZE)
                          .withSort(DEFAULT_DIRECTION, DEFAULT_SORT_CONDITION);
    }
}

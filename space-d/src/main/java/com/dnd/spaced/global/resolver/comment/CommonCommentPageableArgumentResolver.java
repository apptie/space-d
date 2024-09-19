package com.dnd.spaced.global.resolver.comment;

import com.dnd.spaced.global.consts.resolver.ResolverConst;
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
public class CommonCommentPageableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int IGNORED_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_CONDITION = "likeCount";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CommonCommentPageable.class) && parameter.getParameterType()
                                                                                         .equals(Pageable.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Direction direction = findDirection(webRequest.getParameter(ResolverConst.SORT_ORDER_PARAMETER_NAME));
        String sortCondition = findSortCondition(webRequest.getParameter(ResolverConst.SORT_CONDITION_PARAMETER_NAME));

        return PageRequest.of(IGNORED_PAGE, DEFAULT_SIZE)
                          .withSort(direction, sortCondition);
    }

    private Direction findDirection(String target) {
        if (target == null || Direction.DESC.name().equalsIgnoreCase(target)) {
            return Direction.DESC;
        }

        return Direction.ASC;
    }

    private String findSortCondition(String target) {
        if (target == null) {
            return DEFAULT_SORT_CONDITION;
        }

        return target;
    }
}

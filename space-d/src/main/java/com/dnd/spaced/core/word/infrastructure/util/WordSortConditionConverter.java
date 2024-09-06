package com.dnd.spaced.core.word.infrastructure.util;

import static com.dnd.spaced.core.word.domain.QWord.word;

import com.dnd.spaced.global.repository.OrderByNull;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public enum WordSortConditionConverter {

    NAME("name", List.of(word.name, word.updatedAt, word.id));

    private final String sortCondition;
    private final List<ComparableExpressionBase<?>> expressions;

    WordSortConditionConverter(String sortCondition, List<ComparableExpressionBase<?>> expressions) {
        this.sortCondition = sortCondition;
        this.expressions = expressions;
    }

    @SuppressWarnings("unchecked")
    public static List<OrderSpecifier<?>> convert(Pageable pageable) {
        return findOrder(pageable).map(order -> Arrays.stream(WordSortConditionConverter.values())
                                                      .filter(
                                                              converter -> converter.sortCondition.equalsIgnoreCase(
                                                                      order.getProperty()
                                                              )
                                                      )
                                                      .findAny()
                                                      .map(
                                                              converter -> converter.convertOrderSpecifiers(
                                                                      order.getDirection()
                                                              )
                                                      )
                                                      .orElse(List.of(OrderByNull.DEFAULT)))
                                  .orElse(List.of(OrderByNull.DEFAULT));
    }

    private static Optional<Order> findOrder(Pageable pageable) {
        return pageable.getSort()
                       .get()
                       .findAny();
    }

    private List<OrderSpecifier<?>> convertOrderSpecifiers(Direction direction) {
        List<OrderSpecifier<?>> result = new ArrayList<>();

        for (ComparableExpressionBase<?> expression : this.expressions) {
            result.add(applyDirection(expression, direction));
        }

        return result;
    }

    private OrderSpecifier<?> applyDirection(ComparableExpressionBase<?> expression, Direction direction) {
        if (direction.isAscending()) {
            return expression.asc();
        }

        return expression.desc();
    }
}

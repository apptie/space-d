package com.dnd.spaced.config.docs.snippet.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumDocsConverter {

    public static <T extends Enum<T>> Map<String, String> convert(T[] values, Function<T, String> extractor) {
        return Arrays.stream(values)
                     .collect(Collectors.toMap(Enum::name, extractor));
    }
}

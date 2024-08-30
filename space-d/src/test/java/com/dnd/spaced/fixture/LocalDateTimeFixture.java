package com.dnd.spaced.fixture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeFixture {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTimeFixture() {
    }

    public static LocalDateTime from(String dateTime) {
        return LocalDateTime.parse(dateTime, formatter);
    }
}

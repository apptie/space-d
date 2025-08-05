package com.dnd.spaced.core.report.domain.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum ReportStatus {
    PENDING("처리 전"),
    PROCESSED("신고 처리"),
    UN_PROCESSED("신고 반려");

    private final String name;

    ReportStatus(String name) {
        this.name = name;
    }

    public static Optional<ReportStatus> findBy(String name) {
        return Arrays.stream(ReportStatus.values())
                     .filter(status -> status.name().equalsIgnoreCase(name))
                     .findAny();
    }

    public boolean isProcessed() {
        return this == PROCESSED;
    }
}

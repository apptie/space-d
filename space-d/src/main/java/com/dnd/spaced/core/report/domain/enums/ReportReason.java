package com.dnd.spaced.core.report.domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ReportReason {

    SPAM("광고 및 홍보성 내용"),
    PERSONAL_INFORMATION_EXPOSURE("개인정보 노출 위험"),
    OVER_COMMENT("댓글 도배"),
    PROFANITY("욕설, 음란 등 부적절한 내용"),
    ETC("기타");

    private final String cause;

    ReportReason(String cause) {
        this.cause = cause;
    }

    public static Optional<ReportReason> findBy(String cause) {
        return Arrays.stream(ReportReason.values())
                     .filter(reason -> reason.cause.equals(cause))
                     .findAny();
    }
}

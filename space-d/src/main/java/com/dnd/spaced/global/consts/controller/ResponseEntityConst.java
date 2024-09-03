package com.dnd.spaced.global.consts.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseEntityConst {

    public static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent()
                                                                        .build();
    public static final ResponseEntity<Void> OK_NO_BODY = ResponseEntity.ok()
                                                                        .build();
}

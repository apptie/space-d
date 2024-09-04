package com.dnd.spaced.global.consts.controller;

import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseEntityConst {

    private static final String DEFAULT_CONTEXT_PATH = "/";
    private static final URI DEFAULT_CONTEXT_PATH_URI = URI.create(DEFAULT_CONTEXT_PATH);

    public static final ResponseEntity<Void> NO_CONTENT = ResponseEntity.noContent()
                                                                        .build();
    public static final ResponseEntity<Void> OK_NO_BODY = ResponseEntity.ok()
                                                                        .build();
    public static final ResponseEntity<Void> CREATED_DEFAULT_CONTEXT_PATH = ResponseEntity.created(DEFAULT_CONTEXT_PATH_URI)
                                                                                          .build();
}

package com.dnd.spaced.core.word.application.event.listener.exception;

import com.dnd.spaced.global.exception.base.WordServerException;
import com.dnd.spaced.global.exception.code.WordErrorCode;

public class WordNotFoundException extends WordServerException {

    public WordNotFoundException(String message) {
        super(WordErrorCode.WORD_NOT_FOUND_IN_LISTENER_EXCEPTION, message);
    }
}

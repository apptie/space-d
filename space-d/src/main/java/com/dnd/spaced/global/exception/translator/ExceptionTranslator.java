package com.dnd.spaced.global.exception.translator;

import com.dnd.spaced.global.exception.response.ExceptionDto;
import org.springframework.http.HttpStatus;

public interface ExceptionTranslator {

    ExceptionDto translate();

    HttpStatus getHttpStatus();
}

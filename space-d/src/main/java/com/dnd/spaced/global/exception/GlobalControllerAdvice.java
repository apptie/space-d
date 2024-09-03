package com.dnd.spaced.global.exception;

import com.dnd.spaced.global.exception.base.AccountClientException;
import com.dnd.spaced.global.exception.base.AccountServerException;
import com.dnd.spaced.global.exception.base.AuthClientException;
import com.dnd.spaced.global.exception.base.AuthServerException;
import com.dnd.spaced.global.exception.response.ExceptionDto;
import com.dnd.spaced.global.exception.translator.AccountExceptionTranslator;
import com.dnd.spaced.global.exception.translator.AuthExceptionTranslator;
import com.dnd.spaced.global.exception.translator.ExceptionTranslator;
import java.util.stream.Collectors;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "%s : ";

    @ExceptionHandler(AccountServerException.class)
    private ResponseEntity<ExceptionDto> handleAccountServerException(AccountServerException ex) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionTranslator translator = AccountExceptionTranslator.findBy(ex.getErrorCode());

        return ResponseEntity.status(translator.getHttpStatus())
                             .body(translator.translate());
    }

    @ExceptionHandler(AccountClientException.class)
    private ResponseEntity<ExceptionDto> handleAccountClientException(AccountClientException ex) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionTranslator translator = AccountExceptionTranslator.findBy(ex.getErrorCode());

        return ResponseEntity.status(translator.getHttpStatus())
                             .body(translator.translate());
    }

    @ExceptionHandler(AuthServerException.class)
    private ResponseEntity<ExceptionDto> handleAuthServerException(AuthServerException ex) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionTranslator translator = AuthExceptionTranslator.findBy(ex.getErrorCode());

        return ResponseEntity.status(translator.getHttpStatus())
                             .body(translator.translate());
    }

    @ExceptionHandler(AuthClientException.class)
    private ResponseEntity<ExceptionDto> handleAuthClientException(AuthClientException ex) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionTranslator translator = AuthExceptionTranslator.findBy(ex.getErrorCode());

        return ResponseEntity.status(translator.getHttpStatus())
                             .body(translator.translate());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ExceptionDto> handleException(Exception ex) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "EXCEPTION",
                "서버에 문제가 발생했습니다."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        logger.error(String.format(LOG_FORMAT, ex.getClass().getSimpleName()) + "no handle", ex);

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "HTTP_METHOD_NOT_ALLOWED",
                "유효한 HTTP METHOD가 아닙니다."
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "MISSING_PATH_VARIABLE",
                "Path Variable이 누락되었습니다."
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "TYPE_MISMATCH",
                "유효한 타입이 아닙니다."
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "HTTP_MESSAGE_NOT_READABLE",
                "HTTP 메시지를 읽을 수 없습니다."
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        String inputs = ex.getFieldErrors()
                          .stream()
                          .map(FieldError::getField)
                          .collect(Collectors.joining(", "));
        ExceptionDto exceptionDto = new ExceptionDto(
                "INVALID_DATA",
                "유효한 입력 값이 아닙니다. (" + inputs + ")"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "MISSING_SERVLET_REQUEST_PART",
                "RequestPart가 누락되었습니다."
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "HANDLER_NOT_FOUND",
                "요청한 API를 찾을 수 없습니다."
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(exceptionDto);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn(String.format(LOG_FORMAT, ex.getClass().getSimpleName()), ex);

        ExceptionDto exceptionDto = new ExceptionDto(
                "RESOURCE_NOT_FOUND",
                "요청한 리소스를 찾을 수 없습니다."
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(exceptionDto);
    }
}

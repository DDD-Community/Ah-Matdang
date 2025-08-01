package be.ddd.infra.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Map<Class<? extends Exception>, ErrorCode> exceptionMap;

    public GlobalExceptionHandler() {
        Map<Class<? extends Exception>, ErrorCode> tempMap = new HashMap<>();
        for (ErrorCode errorCode : ErrorCode.values()) {
            Set<Class<? extends Exception>> exceptions = errorCode.getExceptions();
            for (Class<? extends Exception> exception : exceptions) {
                tempMap.put(exception, errorCode);
            }
        }

        this.exceptionMap = Collections.unmodifiableMap(tempMap);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        if (exceptionMap.containsKey(e.getClass())) {
            ErrorCode errorCode = exceptionMap.get(e.getClass());
            Object data = parseErrorData(e);
            String message;
            if (data instanceof List<?> errorList
                    && !errorList.isEmpty()
                    && errorList.get(0) instanceof FieldError fe) {
                message = (errorList.size() == 1) ? fe.message() : errorCode.getMessage();
            } else {
                message = errorCode.getMessage();
            }
            ErrorResponse errorRes =
                    ErrorResponse.of(
                            message,
                            errorCode.getStatus().value(),
                            Instant.now(),
                            request.getRequestURI());
            return ResponseEntity.status(errorCode.getStatus()).body(errorRes);
        }

        // 예상 못한 에러
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(
                        ErrorResponse.of(
                                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                                ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(),
                                Instant.now(),
                                request.getRequestURI()));
    }

    private Object parseErrorData(Exception e) {
        return switch (e.getClass().getSimpleName()) {
            case "MethodArgumentNotValidException" ->
                    dataForMethodArgumentNotValidException((MethodArgumentNotValidException) e);
            case "ConstraintViolationException" ->
                    dataForConstraintViolationException((ConstraintViolationException) e);
            default -> null;
        };
    }

    private Object dataForMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .toList();
    }

    private Object dataForConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(
                        violation ->
                                new FieldError(
                                        violation.getPropertyPath().toString(),
                                        violation.getMessage()))
                .toList();
    }

    private record FieldError(String field, String message) {}
}

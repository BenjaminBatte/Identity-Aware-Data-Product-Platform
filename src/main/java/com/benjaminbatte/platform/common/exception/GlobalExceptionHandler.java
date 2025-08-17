package com.benjaminbatte.platform.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex,
                                                        HttpServletRequest req) {
        return build(ex, HttpStatus.NOT_FOUND, req, null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex,
                                                            HttpServletRequest req) {
        return build(ex, HttpStatus.UNAUTHORIZED, req, null);
    }

    @ExceptionHandler(PlatformException.class)
    public ResponseEntity<ErrorResponse> handlePlatform(PlatformException ex,
                                                        HttpServletRequest req) {
        // default platform exception → 400
        return build(ex, HttpStatus.BAD_REQUEST, req, null);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgInvalid(MethodArgumentNotValidException ex,
                                                                HttpServletRequest req) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + (fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid"))
                .collect(toList());
        return build("Validation failed", "VALIDATION_ERROR", HttpStatus.BAD_REQUEST, req, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest req) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(this::formatViolation)
                .collect(toList());
        return build("Validation failed", "VALIDATION_ERROR", HttpStatus.BAD_REQUEST, req, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(HttpMessageNotReadableException ex,
                                                       HttpServletRequest req) {
        return build("Malformed JSON request", "BAD_REQUEST_BODY", HttpStatus.BAD_REQUEST, req, null);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex,
                                                            HttpServletRequest req) {
        return build("Access is denied", "ACCESS_DENIED", HttpStatus.FORBIDDEN, req, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        // avoid leaking internals; keep message generic
        return build("Internal server error", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, req, null);
    }



    private ResponseEntity<ErrorResponse> build(PlatformException ex,
                                                HttpStatus status,
                                                HttpServletRequest req,
                                                List<String> errors) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .code(ex.getErrorCode())
                .path(req.getRequestURI())
                .errors(errors)
                .build();
        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<ErrorResponse> build(String message,
                                                String code,
                                                HttpStatus status,
                                                HttpServletRequest req,
                                                List<String> errors) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .code(code)
                .path(req.getRequestURI())
                .errors(errors)
                .build();
        return new ResponseEntity<>(body, status);
    }

    private String formatViolation(ConstraintViolation<?> v) {
        String field = v.getPropertyPath() != null ? v.getPropertyPath().toString() : "<unknown>";
        String msg = v.getMessage() != null ? v.getMessage() : "invalid";
        return field + ": " + msg;
    }
}

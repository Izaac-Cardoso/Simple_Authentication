package com.cardoso_izaac.SimpleAuthentication.exceptions;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ProblemDetail> duplicatedUser(DuplicatedException e) {
        var error = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getLocalizedMessage());
        error.setProperty("TimeStamp: ", Instant.now());

        return ResponseEntity.ok(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders header,
                                                               HttpStatusCode status, WebRequest request) {

        var problemDetails = ProblemDetail.forStatus(status);
        problemDetails.setTitle("One or more fields are invalid.");

        Map<String, String> fields = ex.getBindingResult().getAllErrors()
                .stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage()));

        problemDetails.setProperty("incorrectFields", fields);

        return handleExceptionInternal(ex, problemDetails, header, status, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> badCredentials(BadCredentialsException e) {
        var error = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        error.setTitle(e.getMessage());
        error.setProperty("TimeStamp: ", Instant.now());

        return ResponseEntity.badRequest().body(error);
    }
}

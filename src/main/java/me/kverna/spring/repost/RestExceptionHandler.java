package me.kverna.spring.repost;

import me.kverna.spring.repost.data.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        FieldError error = ex.getBindingResult().getFieldError();
        String message;
        if (error != null) {
            message = String.format("Failed to validate field %s: %s", error.getField(),
                    error.getDefaultMessage());
        } else {
            message ="Failed to validate: " + ex.getMessage();
        }

        return handleExceptionInternal(ex, new ErrorResponse(message), headers,
                HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        ResponseStatusException statusEx = (ResponseStatusException) ex;

        ErrorResponse response = new ErrorResponse(statusEx.getReason());
        return new ResponseEntity<>(response, statusEx.getResponseHeaders(), statusEx.getStatus());
    }
}

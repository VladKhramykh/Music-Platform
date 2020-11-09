package com.khramykh.platform.api.utils;

import com.khramykh.platform.api.services.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionsHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private ErrorService errorService;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.append(fieldName)
                    .append(":'")
                    .append(errorMessage)
                    .append(";");
        });
        ApiErrorResponse response = new ApiErrorResponse(erros.toString());

//        errorService.save(HttpStatus.BAD_REQUEST, response.getMsg());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
}

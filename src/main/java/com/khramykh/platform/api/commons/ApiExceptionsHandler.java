package com.khramykh.platform.api.commons;

import com.khramykh.platform.application.exceptions.EmailAlreadyInUseException;
import com.khramykh.platform.application.exceptions.ResourceNotFoundException;
import com.khramykh.platform.application.exceptions.TrackNotFoundException;
import com.khramykh.platform.application.exceptions.UserNotFoundException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@PropertySource("classpath:messages.properties")
public class ApiExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundApiException(UserNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse();
        if (!StringUtils.isEmpty(ex.getEmail())) {
            response.setMsg("User not found. Email (" + ex.getEmail() + ")");
        } else if (ex.getId() <= 0) {
            response.setMsg("User not found. Id (" + ex.getId() + ")");
        } else {
            response.setMsg("User not found");
        }

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TrackNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTrackNotFoundApiException(TrackNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse();
        if (!StringUtils.isEmpty(ex.getName())) {
            response.setMsg("{error.track.bynamenotfound} (" + ex.getName() + ")");
        } else if (ex.getId() <= 0) {
            response.setMsg("{error.track.byidnotfound} (" + ex.getId() + ")");
        } else {
            response.setMsg("{error.track.notfound}");
        }

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyInUseApiException(EmailAlreadyInUseException ex) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMsg("{error.email.alreadyinuse}");

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundApiException(ResourceNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMsg(ex.getMsg());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

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
        ApiErrorResponse response = new ApiErrorResponse(errors.toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

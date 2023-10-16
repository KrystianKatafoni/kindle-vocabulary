package com.katafoni.kindlevocabulary.common.exception.api;

import com.katafoni.kindlevocabulary.common.exception.FileUploadException;
import com.katafoni.kindlevocabulary.common.exception.InternalFailureException;
import com.katafoni.kindlevocabulary.common.exception.RequestParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(InternalFailureException.class)
    public ResponseEntity<Object> internalFailureExceptionHandler(InternalFailureException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Object> fileUploadExceptionHandler(FileUploadException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(RequestParameterException.class)
    public ResponseEntity<Object> requestParameterExceptionHandler(RequestParameterException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}

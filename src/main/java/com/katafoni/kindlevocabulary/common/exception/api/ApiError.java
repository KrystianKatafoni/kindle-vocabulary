package com.katafoni.kindlevocabulary.common.exception.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private String errorCode;

    private String debugMessage;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }


    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, String errorCode, String message) {
        this();
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.debugMessage = StringUtils.EMPTY;
    }

    public ApiError(HttpStatus status, String errorCode, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.debugMessage = ex.getLocalizedMessage();
    }
}

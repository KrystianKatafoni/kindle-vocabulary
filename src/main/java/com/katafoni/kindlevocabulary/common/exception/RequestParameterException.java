package com.katafoni.kindlevocabulary.common.exception;

public class RequestParameterException extends KindleVocabularyException{

    public RequestParameterException(String errorCode, String message) {
        super(errorCode, message);
    }

    public RequestParameterException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}

package com.katafoni.kindlevocabulary.common.exception;

public class KindleVocabularyException extends RuntimeException{

    private final String errorCode;


    public KindleVocabularyException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public KindleVocabularyException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

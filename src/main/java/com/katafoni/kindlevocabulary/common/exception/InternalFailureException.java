package com.katafoni.kindlevocabulary.common.exception;

public class InternalFailureException extends KindleVocabularyException {

    public InternalFailureException(String errorCode, String message) {
        super(errorCode, message);
    }
}

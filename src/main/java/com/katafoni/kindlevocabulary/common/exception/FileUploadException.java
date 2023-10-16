package com.katafoni.kindlevocabulary.common.exception;

public class FileUploadException extends KindleVocabularyException{

    public FileUploadException(String errorCode, String message) {
        super(errorCode, message);
    }

    public FileUploadException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}

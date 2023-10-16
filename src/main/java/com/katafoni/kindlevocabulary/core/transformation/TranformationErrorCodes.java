package com.katafoni.kindlevocabulary.core.transformation;

enum TranformationErrorCodes {

    KINDLE_DATABASE_FAILURE("tranformation.kindleDatabaseFailure", "TRF001"),
    LANGUAGE_NAME_INVALID("tranformation.languageNameInvalid", "TRF002");

    private final String messageCode;
    private final String errorCode;

    TranformationErrorCodes(String messageCode, String errorCode) {
        this.messageCode = messageCode;
        this.errorCode = errorCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}

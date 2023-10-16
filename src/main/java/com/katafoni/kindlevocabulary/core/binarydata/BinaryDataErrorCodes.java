package com.katafoni.kindlevocabulary.core.binarydata;

enum BinaryDataErrorCodes {

    DIRECTORY_PATH_FAILURE("binarydata.directoryPathFailure","BDA001"),
    SAVING_FILE_FAILURE("binarydata.savingFileFailure","BDA002"),
    MULTIPART_FILE_FAILURE("binarydata.multipartFileFailure","BDA003"),
    DELETING_FILE_FAILURE("binarydata.deletingFileFailure","BDA004");

    private final String messageCode;
    private final String errorCode;

    BinaryDataErrorCodes(String messageCode, String errorCode) {
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

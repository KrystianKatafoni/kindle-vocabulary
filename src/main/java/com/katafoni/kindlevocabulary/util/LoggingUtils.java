package com.katafoni.kindlevocabulary.util;

public class LoggingUtils {

    public static String getLoggingMessage(String errorCode, String message) {
        return errorCode + " : " + message;
    }
}

package com.katafoni.kindlevocabulary.util;

public interface MessageSourceFacade {
    String getMessage(String messageCode, String... parameters);
}

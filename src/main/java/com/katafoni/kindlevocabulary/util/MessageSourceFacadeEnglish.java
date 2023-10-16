package com.katafoni.kindlevocabulary.util;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Primary
@Component
public class MessageSourceFacadeEnglish implements MessageSourceFacade{

    private MessageSource messageSource;

    private Locale locale = Locale.ENGLISH;

    public MessageSourceFacadeEnglish(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode, String... parameters) {
        return messageSource.getMessage(messageCode, parameters, locale);
    }
}

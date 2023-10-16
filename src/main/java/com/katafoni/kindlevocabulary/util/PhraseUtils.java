package com.katafoni.kindlevocabulary.util;

import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;

import java.util.Set;
import java.util.stream.Collectors;

public class PhraseUtils {

    public static Set<Phrase> filterByLanguage(Set<Phrase> phrases, Language language) {
        ArgumentUtils.checkNotNull(phrases, "phrases");
        ArgumentUtils.checkNotNull(language, "language");

        return phrases.stream().filter(phrase -> phrase.getSourceLanguage().equals(language)).collect(Collectors.toSet());
    }
}

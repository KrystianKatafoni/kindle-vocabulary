package com.katafoni.kindlevocabulary.core.translation;

import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;

import java.util.Set;

interface TranslationProvider {
    Set<Phrase> translate(Set<Phrase> phrases, Language sourceLanguage, Language translationLanguage);
}

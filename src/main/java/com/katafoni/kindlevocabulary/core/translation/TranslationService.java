package com.katafoni.kindlevocabulary.core.translation;

import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;

import java.util.Set;

public interface TranslationService {
    Set<Phrase> translatePhrases(Set<Phrase> phrases, Language translationLanguage);
}

package com.katafoni.kindlevocabulary.core.language;

import com.katafoni.kindlevocabulary.domain.entity.Language;

import java.util.Optional;

public interface LanguageService {
    Optional<Language> getLanguageByAbbreviation(String abbr);

    Optional<Language> getLanguageByName(String name);
}

package com.katafoni.kindlevocabulary.core.language;

import com.katafoni.kindlevocabulary.domain.entity.Language;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService{

    private LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Optional<Language> getLanguageByAbbreviation(String abbr) {
        return this.languageRepository.findByAbbreviationIgnoreCase(abbr);
    }

    @Override
    public Optional<Language> getLanguageByName(String name) {
        return this.languageRepository.findByLanguageNameIgnoreCase(name);
    }
}

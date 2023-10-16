package com.katafoni.kindlevocabulary.core.transformation;

import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;
import com.katafoni.kindlevocabulary.core.language.LanguageService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class KindleWordPhraseMapping {

    @Autowired
    private LanguageService languageService;

    public Phrase kindleWordToPhrase(KindleWord kindleWord) {
        Phrase phrase = new Phrase();
        phrase.setSourceText(kindleWord.getWord());
        Optional<Language> languageOpt =
                this.languageService.getLanguageByAbbreviation(kindleWord.getLanguage());
        Language language = languageOpt.orElse(null);
        phrase.setSourceLanguage(language);
        return phrase;
    }

    public abstract Set<Phrase>  kindleWordsToPhrases(Set<KindleWord> kindleWords);
}

package com.katafoni.kindlevocabulary.core.translation;


import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("prod")
@Primary
@Component
class GoogleTranslateProviderProd extends AbstractGoogleTranslateProvider implements TranslationProvider {

    @Override
    public Set<Phrase> translate(Set<Phrase> phrases, Language sourceLanguage, Language translationLanguage) {
        List<Phrase> phrasesList = phrases.stream().collect(Collectors.toList());
        return this.translate(phrasesList, sourceLanguage, translationLanguage);
    }
}


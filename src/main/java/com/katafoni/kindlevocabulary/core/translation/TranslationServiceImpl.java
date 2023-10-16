package com.katafoni.kindlevocabulary.core.translation;

import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TranslationServiceImpl implements TranslationService{

    private TranslationProvider translationProvider;

    public TranslationServiceImpl(TranslationProvider translationProvider) {
        this.translationProvider = translationProvider;
    }

    @Override
    public Set<Phrase> translatePhrases(Set<Phrase> phrases, Language translationLanguage) {
        if(phrases.isEmpty()){
            return phrases;
        }

        Language sourceLanguage = phrases.stream().findAny().get().getSourceLanguage();
        return translationProvider.translate(phrases, sourceLanguage, translationLanguage);
    }
}

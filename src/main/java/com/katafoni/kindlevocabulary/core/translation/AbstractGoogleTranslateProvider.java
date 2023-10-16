package com.katafoni.kindlevocabulary.core.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.Sets;
import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractGoogleTranslateProvider{

    public Set<Phrase> translate(List<Phrase> phrasesList, Language sourceLanguage, Language translationLanguage) {
        List<String> words = phrasesList.stream().map(Phrase::getSourceText).collect(Collectors.toList());
        Translate service = TranslateOptions.getDefaultInstance().getService();
        List<Translation> translationResults = service.translate(words,
                Translate.TranslateOption.sourceLanguage(sourceLanguage.getAbbreviation()),
                Translate.TranslateOption.targetLanguage(translationLanguage.getAbbreviation()));
        List<String> translations =
                translationResults.stream().map(Translation::getTranslatedText).collect(Collectors.toList());
        for (int i = 0; i < phrasesList.size(); i++) {
            phrasesList.get(i).setTranslatedText(translations.get(i));
        }

        return Sets.newHashSet(phrasesList);
    }
}

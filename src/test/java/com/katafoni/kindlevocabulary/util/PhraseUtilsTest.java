package com.katafoni.kindlevocabulary.util;

import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;
import com.katafoni.util.creators.PhraseCreator;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PhraseUtilsTest {

    private Language germanSourceLanguage = new Language("german", "de");
    private Set<Phrase> testPhrases = PhraseCreator.createPhrasesWithAdditionalSourceLanguage(11, germanSourceLanguage);

    @Test
    void whenFilterByLanguageEnglish_thenFivePhrasesInResult() {

        //when
        Set<Phrase> resultPhrasesEnglish = PhraseUtils.filterByLanguage(this.testPhrases, PhraseCreator.SOURCE_LANGUAGE);

        //then
        assertEquals(5, resultPhrasesEnglish.size());
        assertThat(resultPhrasesEnglish).extracting(Phrase::getSourceLanguage).containsOnly(PhraseCreator.SOURCE_LANGUAGE);
    }

    @Test
    void whenFilterByLanguageGerman_thenSixPhrasesInResult() {

        //when
        Set<Phrase> resultPhrasesGerman = PhraseUtils.filterByLanguage(this.testPhrases, this.germanSourceLanguage);

        //then
        assertEquals(6, resultPhrasesGerman.size());
        assertThat(resultPhrasesGerman).extracting(Phrase::getSourceLanguage).containsOnly(this.germanSourceLanguage);
    }

    @Test
    void whenFilterByLanguageRussian_thenZeroPhrasesInResult() {

        //given
        Language russianSourceLanguage = new Language("russian", "ru");

        //when
        Set<Phrase> resultPhrases = PhraseUtils.filterByLanguage(this.testPhrases, russianSourceLanguage);

        //then
        assertTrue(resultPhrases.isEmpty());
    }

    @Test
    void whenFilterByLanguageEmptyPhrases_thenReturnEmptyPhrases() {

        //when
        Set<Phrase> resultPhrases = PhraseUtils.filterByLanguage(Sets.newHashSet(), PhraseCreator.SOURCE_LANGUAGE);

        //then
        assertTrue(resultPhrases.isEmpty());
    }

    @Test
    void whenFilterByLanguagePhrasesNull_thenThrowException() {

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> PhraseUtils.filterByLanguage(null, this.germanSourceLanguage));

        //then
        assertEquals("Argument phrases cannot be null", exception.getMessage());
    }

    @Test
    void whenFilterByLanguageLanguageNull_thenThrowException() {

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> PhraseUtils.filterByLanguage(testPhrases, null));

        //then
        assertEquals("Argument language cannot be null", exception.getMessage());
    }
}
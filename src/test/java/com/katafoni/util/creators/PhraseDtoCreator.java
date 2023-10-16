package com.katafoni.util.creators;

import com.katafoni.kindlevocabulary.domain.dto.PhraseDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class PhraseDtoCreator {

    public final static String TRANSLATED_LANGUAGE = "polish";
    public final static String SOURCE_LANGUAGE = "english";

    public static Set<PhraseDto> createPhraseDtosCollection(long size) {
        return LongStream.range(0, size)
                .mapToObj(longNum -> createPhraseDto(longNum))
                .collect(Collectors.toSet());
    }

    private static PhraseDto createPhraseDto(long id) {
        return PhraseDto.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusHours(1))
                .id(id)
                .sourceLanguage(SOURCE_LANGUAGE)
                .translatedLanguage(TRANSLATED_LANGUAGE)
                .sourceText(RandomStringUtils.random(5, true, false))
                .translatedText(RandomStringUtils.random(5, true, false))
                .build();
    }
}

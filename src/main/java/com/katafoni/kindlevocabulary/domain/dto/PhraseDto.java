package com.katafoni.kindlevocabulary.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhraseDto {

    private long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String sourceText;

    private String translatedText;

    private String sourceLanguage;

    private String translatedLanguage;
}

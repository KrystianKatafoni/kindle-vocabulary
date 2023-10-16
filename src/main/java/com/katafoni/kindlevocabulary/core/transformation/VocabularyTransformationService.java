package com.katafoni.kindlevocabulary.core.transformation;

import com.katafoni.kindlevocabulary.domain.dto.PhraseDto;

import java.util.Set;

public interface VocabularyTransformationService {
    Set<PhraseDto> transformVocabulary(TransformationRequest transformationRequest);
}

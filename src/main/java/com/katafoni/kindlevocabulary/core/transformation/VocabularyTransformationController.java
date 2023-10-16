package com.katafoni.kindlevocabulary.core.transformation;

import com.katafoni.kindlevocabulary.domain.dto.PhraseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Set;

@RestController
@RequestMapping(value = "/files/vocabulary")
public class VocabularyTransformationController {

    private final static Logger logger = LoggerFactory.getLogger(VocabularyTransformationController.class);

    private VocabularyTransformationService vocabularyTransformationService;

    public VocabularyTransformationController(VocabularyTransformationService vocabularyTransformationService) {
        this.vocabularyTransformationService = vocabularyTransformationService;
    }

    @GetMapping
    public Set<PhraseDto> getVocabularyFromFile(@RequestParam("file") @NotNull MultipartFile file,
                                                @RequestParam(value = "trans_lang", required = false) String translationLanguage,
                                                @RequestParam(value = "filter_lang") @NotNull String filteringLanguage) {
        logger.info("New transformation request for vocabulary set, filename: {}, translation language: {}, " +
                        "filtering language: {}", file.getOriginalFilename(), translationLanguage, filteringLanguage);

        TransformationRequest transformationRequest = new TransformationRequest(file, translationLanguage,
                filteringLanguage);
        Set<PhraseDto> vocabularySet = this.vocabularyTransformationService.transformVocabulary(transformationRequest);

        logger.info("Vocabulary set with phrases created. Size: {}", vocabularySet.size());

        return vocabularySet;
    }
}

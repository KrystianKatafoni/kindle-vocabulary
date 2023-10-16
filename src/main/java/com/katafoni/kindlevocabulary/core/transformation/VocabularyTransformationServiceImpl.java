package com.katafoni.kindlevocabulary.core.transformation;

import com.katafoni.kindlevocabulary.common.exception.ExceptionMessageCodes;
import com.katafoni.kindlevocabulary.common.exception.RequestParameterException;
import com.katafoni.kindlevocabulary.common.mapping.PhraseAndPhraseDtoMapper;
import com.katafoni.kindlevocabulary.core.language.LanguageService;
import com.katafoni.kindlevocabulary.core.translation.TranslationService;
import com.katafoni.kindlevocabulary.domain.dto.PhraseDto;
import com.katafoni.kindlevocabulary.domain.entity.Language;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;
import com.katafoni.kindlevocabulary.util.LoggingUtils;
import com.katafoni.kindlevocabulary.util.MessageSourceFacade;
import com.katafoni.kindlevocabulary.util.PhraseUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
class VocabularyTransformationServiceImpl implements VocabularyTransformationService {

    private static final Logger logger = LoggerFactory.getLogger(VocabularyTransformationServiceImpl.class);

    private FileConverter fileConverter;

    private TranslationService translationService;

    private PhraseAndPhraseDtoMapper phraseAndPhraseDtoMapper;

    private LanguageService languageService;

    private MessageSourceFacade messageSourceFacade;

    public VocabularyTransformationServiceImpl(FileConverter fileConverter, TranslationService translationService,
                                               PhraseAndPhraseDtoMapper phraseAndPhraseDtoMapper,
                                               LanguageService languageService,
                                               MessageSourceFacade messageSourceFacade) {
        this.fileConverter = fileConverter;
        this.translationService = translationService;
        this.phraseAndPhraseDtoMapper = phraseAndPhraseDtoMapper;
        this.languageService = languageService;
        this.messageSourceFacade = messageSourceFacade;
    }

    @Override
    public Set<PhraseDto> transformVocabulary(TransformationRequest transformationRequest) {

        Set<Phrase> phrases = this.fileConverter.convertFileToPhrases(transformationRequest.getMultipartFile());
        phrases = removePhrasesWithoutLanguage(phrases);

        Optional<Language> filteringLanguage =
                languageService.getLanguageByName(transformationRequest.getFilteringLanguage());

        if (filteringLanguage.isPresent()) {
            int initialPhrasesCount = phrases.size();

            phrases = PhraseUtils.filterByLanguage(phrases, filteringLanguage.get());

            int filteredPhrases = initialPhrasesCount - phrases.size();

            logger.info("There was {} filtered phrases. Filtering language: {}", filteredPhrases,
                    filteringLanguage.get().getLanguageName());
        } else {
            String message = messageSourceFacade.getMessage(TranformationErrorCodes.LANGUAGE_NAME_INVALID.getMessageCode(),
                    transformationRequest.getFilteringLanguage());
            logger.warn(LoggingUtils.getLoggingMessage(TranformationErrorCodes.LANGUAGE_NAME_INVALID.getErrorCode(), message));
            throw new RequestParameterException(TranformationErrorCodes.LANGUAGE_NAME_INVALID.getErrorCode(),
                    messageSourceFacade.getMessage(ExceptionMessageCodes.REQUEST_PARAMETER_EXCEPTION,
                            "filter_lang", transformationRequest.getFilteringLanguage()));
        }

        if (StringUtils.isNotBlank(transformationRequest.getTranslationLanguage())) {

            Optional<Language> translationLanguage =
                    languageService.getLanguageByName(transformationRequest.getTranslationLanguage());

            if (translationLanguage.isPresent()) {
                phrases = translationService.translatePhrases(phrases, translationLanguage.get());
            } else {
                String message = messageSourceFacade.getMessage(TranformationErrorCodes.LANGUAGE_NAME_INVALID.getMessageCode(),
                        transformationRequest.getTranslationLanguage());
                logger.warn(LoggingUtils.getLoggingMessage(TranformationErrorCodes.LANGUAGE_NAME_INVALID.getErrorCode(), message));
                throw new RequestParameterException(TranformationErrorCodes.LANGUAGE_NAME_INVALID.getErrorCode(),
                        messageSourceFacade.getMessage(ExceptionMessageCodes.REQUEST_PARAMETER_EXCEPTION,
                                "trans_lang", transformationRequest.getTranslationLanguage()));
            }

        }

        return this.phraseAndPhraseDtoMapper.phrasesToPhraseDtos(phrases);
    }

    private Set<Phrase> removePhrasesWithoutLanguage(Set<Phrase> phrases) {

        int initialPhrasesCount = phrases.size();

        phrases = phrases.stream().filter(phrase -> Objects.nonNull(phrase.getSourceLanguage())).collect(Collectors.toSet());

        int afterRemovingPhrasesCount = phrases.size();
        int deletedPhrasesCount = initialPhrasesCount - afterRemovingPhrasesCount;
        logger.info("There was {} deleted phrases without language", deletedPhrasesCount);
        return phrases;
    }
}

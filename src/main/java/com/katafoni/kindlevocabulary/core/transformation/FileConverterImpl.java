package com.katafoni.kindlevocabulary.core.transformation;

import com.katafoni.kindlevocabulary.core.binarydata.BinaryDataService;
import com.katafoni.kindlevocabulary.domain.entity.Phrase;
import com.katafoni.kindlevocabulary.common.properties.BinaryDataProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
class FileConverterImpl implements FileConverter {

    private BinaryDataService binaryDataService;

    private DatabaseReader databaseReader;

    private BinaryDataProperties binaryDataProperties;

    private KindleWordPhraseMapping mapper;

    public FileConverterImpl(BinaryDataService binaryDataService, DatabaseReader databaseReader,
                             BinaryDataProperties binaryDataProperties, KindleWordPhraseMapping mapper) {
        this.binaryDataService = binaryDataService;
        this.databaseReader = databaseReader;
        this.binaryDataProperties = binaryDataProperties;
        this.mapper = mapper;
    }

    @Override
    public Set<Phrase> convertFileToPhrases(MultipartFile multipartFile) {
        String fileName = this.binaryDataService.saveFileAsDatabaseStorage(multipartFile);
        Set<KindleWord> kindleWords =
                this.databaseReader.readWordsFromKindleDatabase(binaryDataProperties.getLocalStoragePath() + fileName);
        Set<Phrase> phrases = this.mapper.kindleWordsToPhrases(kindleWords);

        this.binaryDataService.deleteFileSavedAsDatabaseStorage(fileName);

        return phrases;
    }
}

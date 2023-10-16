package com.katafoni.kindlevocabulary.core.binarydata;

import com.katafoni.kindlevocabulary.common.exception.ExceptionMessageCodes;
import com.katafoni.kindlevocabulary.common.exception.InternalFailureException;
import com.katafoni.kindlevocabulary.common.properties.BinaryDataProperties;
import com.katafoni.kindlevocabulary.util.ArgumentUtils;
import com.katafoni.kindlevocabulary.util.LoggingUtils;
import com.katafoni.kindlevocabulary.util.MessageSourceFacade;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static com.katafoni.kindlevocabulary.core.binarydata.BinaryDataErrorCodes.*;
import static com.katafoni.kindlevocabulary.util.DateTime.FILE_DATETIME_FORMATTER;

@Component
class LocalStorageProvider implements BinaryDataProvider {

    private static final Logger logger = LoggerFactory.getLogger(LocalStorageProvider.class);

    private BinaryDataProperties binaryDataProperties;

    private MessageSourceFacade messageSource;

    public LocalStorageProvider(BinaryDataProperties binaryDataProperties, MessageSourceFacade messageSource) {
        this.binaryDataProperties = binaryDataProperties;
        this.messageSource = messageSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String saveFile(InputStream inputStream) {
        ArgumentUtils.checkNotNull(inputStream, "inputStream");

        validateInternalPathConfiguration();
        String fileName =
                binaryDataProperties.getTemporaryDatabaseFilename()
                        + LocalDateTime.now().format(FILE_DATETIME_FORMATTER)
                        + binaryDataProperties.getTemporaryDatabaseFileExtension();

        File targetFile = new File(binaryDataProperties.getLocalStoragePath() + fileName);

        try {
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
        } catch (IOException e) {
            String message = messageSource.getMessage(SAVING_FILE_FAILURE.getMessageCode(), e.getMessage());
            logger.error(LoggingUtils.getLoggingMessage(SAVING_FILE_FAILURE.getErrorCode(), message));
            throw new InternalFailureException(SAVING_FILE_FAILURE.getErrorCode(),
                    messageSource.getMessage(ExceptionMessageCodes.INTERNAL_FAILURE_EXCEPTION));
        }
        return fileName;
    }

    @Override
    public void deleteFile(String fileName) {
        Path path = Paths.get(binaryDataProperties.getLocalStoragePath() + fileName);
        try {
            Files.delete(path);
        } catch (IOException e) {
            String message = messageSource.getMessage(DELETING_FILE_FAILURE.getMessageCode(), e.getMessage());
            logger.error(LoggingUtils.getLoggingMessage(DELETING_FILE_FAILURE.getErrorCode(), message));
        }
    }

    private void validateInternalPathConfiguration() {
        Path path = Paths.get(binaryDataProperties.getLocalStoragePath());
        if (!Files.isDirectory(path)) {
            String message = messageSource.getMessage(DIRECTORY_PATH_FAILURE.getMessageCode(),
                    binaryDataProperties.getLocalStoragePath());
            logger.error(LoggingUtils.getLoggingMessage(DIRECTORY_PATH_FAILURE.getErrorCode(), message));
            throw new InternalFailureException(DIRECTORY_PATH_FAILURE.getErrorCode(),
                    messageSource.getMessage(ExceptionMessageCodes.INTERNAL_FAILURE_EXCEPTION));
        }
    }

    @Override
    public BinaryDataProviderName getBinaryDataProviderName() {
        return BinaryDataProviderName.LOCAL_STORAGE;
    }
}

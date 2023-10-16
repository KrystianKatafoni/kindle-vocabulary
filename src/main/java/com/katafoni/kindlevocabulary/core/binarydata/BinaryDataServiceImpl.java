package com.katafoni.kindlevocabulary.core.binarydata;

import com.katafoni.kindlevocabulary.common.exception.ExceptionMessageCodes;
import com.katafoni.kindlevocabulary.common.exception.FileUploadException;
import com.katafoni.kindlevocabulary.common.properties.BinaryDataProperties;
import com.katafoni.kindlevocabulary.util.ArgumentUtils;
import com.katafoni.kindlevocabulary.util.LoggingUtils;
import com.katafoni.kindlevocabulary.util.MessageSourceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BinaryDataServiceImpl implements BinaryDataService {

    private static final Logger logger = LoggerFactory.getLogger(BinaryDataServiceImpl.class);

    private BinaryDataProperties binaryDataProperties;

    private BinaryDataProviderFactory binaryDataProviderFactory;

    private MessageSourceFacade messageSource;

    public BinaryDataServiceImpl(BinaryDataProperties binaryDataProperties,
                                 BinaryDataProviderFactory binaryDataProviderFactory,
                                 MessageSourceFacade messageSource) {
        this.binaryDataProperties = binaryDataProperties;
        this.binaryDataProviderFactory = binaryDataProviderFactory;
        this.messageSource = messageSource;
    }

    @Override
    public String saveFileAsDatabaseStorage(MultipartFile multipartFile) {
        ArgumentUtils.checkNotNull(multipartFile, "multipartFile");

        BinaryDataProvider binaryDataProvider =
                binaryDataProviderFactory.findBinaryDataProvider(BinaryDataProviderName.LOCAL_STORAGE);
        String fileName;
        try {
            fileName = binaryDataProvider.saveFile(multipartFile.getInputStream());
        } catch (IOException e) {
            String message = messageSource.getMessage(BinaryDataErrorCodes.MULTIPART_FILE_FAILURE.getMessageCode(), e.getMessage());
            logger.error(LoggingUtils.getLoggingMessage(BinaryDataErrorCodes.MULTIPART_FILE_FAILURE.getErrorCode(), message));
            throw new FileUploadException(BinaryDataErrorCodes.MULTIPART_FILE_FAILURE.getErrorCode(),
                    messageSource.getMessage(ExceptionMessageCodes.FILE_UPLOAD_EXCEPTION));
        }
        return fileName;
    }

    @Override
    public void deleteFileSavedAsDatabaseStorage(String fileName) {
        ArgumentUtils.checkNotEmpty(fileName, "fileName");

        BinaryDataProvider binaryDataProvider =
                binaryDataProviderFactory.findBinaryDataProvider(BinaryDataProviderName.LOCAL_STORAGE);
        binaryDataProvider.deleteFile(fileName);
    }
}

package com.katafoni.kindlevocabulary.common.properties;

import com.katafoni.kindlevocabulary.core.binarydata.BinaryDataProviderName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "binary")

public class BinaryDataProperties {
    private BinaryDataProviderName defaultStorage;
    private String localStoragePath;
    private String temporaryDatabaseFilename;
    private String temporaryDatabaseFileExtension;
}

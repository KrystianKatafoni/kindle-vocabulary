package com.katafoni.kindlevocabulary.core.binarydata;

import java.io.InputStream;

interface BinaryDataProvider {

    String saveFile(InputStream inputStream);

    void deleteFile(String fileName);

    BinaryDataProviderName getBinaryDataProviderName();
}

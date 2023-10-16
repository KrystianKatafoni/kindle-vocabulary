package com.katafoni.kindlevocabulary.core.binarydata;

import org.springframework.web.multipart.MultipartFile;

public interface BinaryDataService {

    String saveFileAsDatabaseStorage(MultipartFile multipartFile);

    void deleteFileSavedAsDatabaseStorage(String fileName);
}

package com.katafoni.kindlevocabulary.core.transformation;

import com.katafoni.kindlevocabulary.domain.entity.Phrase;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

interface FileConverter {

    Set<Phrase> convertFileToPhrases(MultipartFile multipartFile);
}

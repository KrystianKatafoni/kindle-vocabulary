package com.katafoni.kindlevocabulary.core.transformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformationRequest {

    private MultipartFile multipartFile;

    private String translationLanguage;

    private String filteringLanguage;
}

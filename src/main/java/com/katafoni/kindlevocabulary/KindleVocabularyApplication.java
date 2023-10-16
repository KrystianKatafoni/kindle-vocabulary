package com.katafoni.kindlevocabulary;

import com.katafoni.kindlevocabulary.common.properties.BinaryDataProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        BinaryDataProperties.class
})
public class KindleVocabularyApplication {

    public static void main(String[] args) {
        SpringApplication.run(KindleVocabularyApplication.class, args);
    }

}

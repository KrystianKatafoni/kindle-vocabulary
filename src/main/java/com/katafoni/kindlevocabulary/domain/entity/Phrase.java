package com.katafoni.kindlevocabulary.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Phrase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @NotBlank(message = "Sourcetext is mandatory")
    private String sourceText;

    private String translatedText;

    @ManyToOne()
    @JoinColumn(name = "source_language_id")
    private Language sourceLanguage;

    @ManyToOne()
    @JoinColumn(name = "translated_language_id")
    private Language translatedLanguage;

}

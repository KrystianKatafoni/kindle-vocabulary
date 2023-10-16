package com.katafoni.kindlevocabulary.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Language {

    public Language(String languageName, String abbreviation) {
        this.languageName = languageName;
        this.abbreviation = abbreviation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "language_name")
    private String languageName;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return languageName.equalsIgnoreCase(language.languageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageName);
    }
}

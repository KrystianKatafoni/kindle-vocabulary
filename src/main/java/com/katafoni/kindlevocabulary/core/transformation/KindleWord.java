package com.katafoni.kindlevocabulary.core.transformation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
class KindleWord {

    private String id;

    private String word;

    private String stem;

    private String language;

    private String timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KindleWord that = (KindleWord) o;
        return word.equals(that.word) && language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, language);
    }
}

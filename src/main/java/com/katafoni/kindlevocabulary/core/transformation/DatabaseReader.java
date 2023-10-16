package com.katafoni.kindlevocabulary.core.transformation;

import java.util.Set;

interface DatabaseReader {
    Set<KindleWord> readWordsFromKindleDatabase(String databasePath);
}

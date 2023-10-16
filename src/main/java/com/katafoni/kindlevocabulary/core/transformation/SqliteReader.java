package com.katafoni.kindlevocabulary.core.transformation;

import com.katafoni.kindlevocabulary.common.exception.ExceptionMessageCodes;
import com.katafoni.kindlevocabulary.common.exception.InternalFailureException;
import com.katafoni.kindlevocabulary.util.LoggingUtils;
import com.katafoni.kindlevocabulary.util.MessageSourceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@Component
class SqliteReader implements DatabaseReader {

    private static final Logger logger = LoggerFactory.getLogger(SqliteReader.class);

    private MessageSourceFacade messageSourceFacade;

    public SqliteReader(MessageSourceFacade messageSourceFacade) {
        this.messageSourceFacade = messageSourceFacade;
    }

    @Override
    public Set<KindleWord> readWordsFromKindleDatabase(String databasePath) {
        Connection connection = null;
        Set<KindleWord> kindleWords = new HashSet<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from WORDS");
            while (rs.next()) {
                KindleWord kindleWord = new KindleWord();
                kindleWord.setId(rs.getString("id"));
                kindleWord.setWord(rs.getString("word"));
                kindleWord.setStem(rs.getString("stem"));
                kindleWord.setLanguage(rs.getString("lang"));
                kindleWord.setTimestamp(rs.getString("timestamp"));
                kindleWords.add(kindleWord);
            }
        } catch (SQLException e) {
            String message = messageSourceFacade.getMessage(TranformationErrorCodes.KINDLE_DATABASE_FAILURE.getMessageCode(), databasePath,
                    e.getMessage());
            logger.error(LoggingUtils.getLoggingMessage(TranformationErrorCodes.KINDLE_DATABASE_FAILURE.getErrorCode(), message));
            throw new InternalFailureException(TranformationErrorCodes.KINDLE_DATABASE_FAILURE.getErrorCode(),
                    messageSourceFacade.getMessage(ExceptionMessageCodes.INTERNAL_FAILURE_EXCEPTION));
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                String message = messageSourceFacade.getMessage(TranformationErrorCodes.KINDLE_DATABASE_FAILURE.getMessageCode(),
                        databasePath, e.getMessage());
                logger.error(LoggingUtils.getLoggingMessage(TranformationErrorCodes.KINDLE_DATABASE_FAILURE.getErrorCode(), message));
            }
        }
        return kindleWords;
    }
}


package com.dukeacademy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.dukeacademy.commons.core.LogsCenter;
import com.dukeacademy.commons.exceptions.DataConversionException;

import com.dukeacademy.model.prefs.ReadOnlyUserPrefs;
import com.dukeacademy.model.prefs.UserPrefs;
import com.dukeacademy.model.question.QuestionBank;
import com.dukeacademy.storage.prefs.UserPrefsStorage;
import com.dukeacademy.storage.question.QuestionBankStorage;

/**
 * Manages storage of QuestionBank data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private QuestionBankStorage questionBankStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(QuestionBankStorage questionBankStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.questionBankStorage = questionBankStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ QuestionBank methods ==============================

    @Override
    public Path getQuestionBankFilePath() {
        return questionBankStorage.getQuestionBankFilePath();
    }

    @Override
    public Optional<QuestionBank> readQuestionBank() throws DataConversionException, IOException {
        return readQuestionBank(questionBankStorage.getQuestionBankFilePath());
    }

    @Override
    public Optional<QuestionBank> readQuestionBank(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return questionBankStorage.readQuestionBank(filePath);
    }

    @Override
    public void saveQuestionBank(QuestionBank questionBank) throws IOException {
        saveQuestionBank(questionBank, questionBankStorage.getQuestionBankFilePath());
    }

    @Override
    public void saveQuestionBank(QuestionBank questionBank, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        questionBankStorage.saveQuestionBank(questionBank, filePath);
    }

}

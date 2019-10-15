package com.dukeacademy.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.dukeacademy.commons.core.LogsCenter;
import com.dukeacademy.commons.exceptions.DataConversionException;
import com.dukeacademy.commons.exceptions.IllegalValueException;
import com.dukeacademy.commons.util.FileUtil;
import com.dukeacademy.commons.util.JsonUtil;
import com.dukeacademy.model.ReadOnlyQuestionBank;

import com.dukeacademy.model.solution.TestCase;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A class to access QuestionBank data stored as a json file on the hard disk.
 */
public class JsonQuestionBankStorage implements QuestionBankStorage {

    private static final Logger logger = LogsCenter.getLogger(
        JsonQuestionBankStorage.class);

    private Path filePath;

    /**
     * Instantiates a new Json question bank storage.
     *
     * @param filePath the file path
     */
    public JsonQuestionBankStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getQuestionBankFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyQuestionBank> readQuestionBank() throws DataConversionException {
        return readQuestionBank(filePath);
    }

    /**
     * Similar to {@link #readQuestionBank()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyQuestionBank> readQuestionBank(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableQuestionBank> jsonQuestionBank =
            JsonUtil.readJsonFile(
                filePath, JsonSerializableQuestionBank.class);
        if (!jsonQuestionBank.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonQuestionBank.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveQuestionBank(ReadOnlyQuestionBank questionBank) throws IOException {
        saveQuestionBank(questionBank, filePath);
    }

    /**
     * Similar to {@link #saveQuestionBank(ReadOnlyQuestionBank)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveQuestionBank(ReadOnlyQuestionBank questionBank, Path filePath) throws IOException {
        requireNonNull(questionBank);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableQuestionBank(questionBank), filePath);
    }
}

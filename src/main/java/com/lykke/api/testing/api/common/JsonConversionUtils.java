package com.lykke.api.testing.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class JsonConversionUtils {

    @SneakyThrows
    public static <E> String convertToJson(E objectToConvert) {
        val mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(objectToConvert);
    }

    @SneakyThrows
    public static <E> E convertFromJson(String jsonToConvert, Class<E> type) {
        val mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.readValue(jsonToConvert, type);
    }

    @SneakyThrows
    public static <E> E convertFromJsonFile(String pathToFile, Class<E> type) {
        val mapper = new ObjectMapper();
        return mapper.readValue(new File(pathToFile), type);
    }
}

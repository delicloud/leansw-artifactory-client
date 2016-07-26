package com.thoughtworks.lean.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.github.fge.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class.getSimpleName());


    public static <T> T parseJSON(String string) throws IOException {
        return JacksonUtils.newMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(string, new TypeReference<T>() {
        });
    }

    public static <T extends Object> T parseJSON(String string, Class<T> tClass) throws IOException {
        return JacksonUtils.newMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(string, tClass);
    }

    public static <T extends Object> T parseJSON(String string, TypeReference<T> tTypeReference) throws IOException {
        return JacksonUtils.newMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(string, tTypeReference);
    }

}
package com.mvntest.sumerge.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.uuid.Generators;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.Locale.ENGLISH;

public final class Utilities {

    private Utilities() {
    }

    public static String generateUUID() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public static String generateTimestampUTC() {
        LocalDateTime localDateTime = LocalDateTime.now(Clock.systemUTC());
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static String getMessageAr(String key) {
        return ResourceBundle.getBundle("messages_ar").getString(key);
    }

    public static String getMessageEn(String key) {
        return ResourceBundle.getBundle("messages_en", ENGLISH).getString(key);
    }

}

package com.usermind.elidetest.helpers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.text.SimpleDateFormat;

public class JsonSerialization {

    public static final String DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm";

    private JsonSerialization() {
    }

    /**
     * One place to configure Jackson consistently
     */
    public static void configure(ObjectMapper objectMapper) {
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JodaModule());
        objectMapper.disableDefaultTyping();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //print dates as human readable. It should take a dateformat, but
        //it doesn't seem to pay any attention to which dateformat I feed in.
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT));
    }

    public static void configureWithTypeInformation(ObjectMapper objectMapper) {
        configure(objectMapper);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

}

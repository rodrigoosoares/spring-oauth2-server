package com.personal.study.springoauth2server.converters;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.SerializationUtils;

public class SerializableObjectConverter {

    private SerializableObjectConverter() {}

    public static String serialize(final Object object) {
        final byte[] bytes = SerializationUtils.serialize(object);
        return Base64.encodeBase64String(bytes);
    }

    public static Object deserialize(final String encodedObject) {
        final byte[] bytes = Base64.decodeBase64(encodedObject);
        return SerializationUtils.deserialize(bytes);
    }

}

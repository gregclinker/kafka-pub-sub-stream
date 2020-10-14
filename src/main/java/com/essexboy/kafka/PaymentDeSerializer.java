package com.essexboy.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentDeSerializer implements Deserializer<Payment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDeSerializer.class);

    @Override
    public Payment deserialize(String s, byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new String(bytes), Payment.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("error", e);
        }
        return null;
    }
}

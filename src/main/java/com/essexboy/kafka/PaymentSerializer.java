package com.essexboy.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentSerializer implements Serializer<Payment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSerializer.class);

    @Override
    public byte[] serialize(String s, Payment payment) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(payment).getBytes();
        } catch (JsonProcessingException e) {
            LOGGER.error("error", e);
        }
        return new byte[0];
    }
}

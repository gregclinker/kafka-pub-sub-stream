package com.essexboy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    public final CountDownLatch latch1 = new CountDownLatch(1);

    @KafkaListener(id = "message", topics = "payments")
    public void paymentListener(String message) {
        LOGGER.debug("received message:" + message);
        this.latch1.countDown();
    }
}
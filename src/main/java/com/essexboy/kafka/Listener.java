package com.essexboy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    public final CountDownLatch latch1 = new CountDownLatch(1);
    private final ApplicationEventPublisher applicationEventPublisher;

    Listener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @KafkaListener(id = "message", topics = "payments")
    public void paymentListener(Payment payment) {
        LOGGER.debug("received payment:" + payment);
        applicationEventPublisher.publishEvent(new PaymentCreatedEvent(payment));
        this.latch1.countDown();
    }
}
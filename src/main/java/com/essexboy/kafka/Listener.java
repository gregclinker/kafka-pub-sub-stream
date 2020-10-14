package com.essexboy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    Listener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public final CountDownLatch latch1 = new CountDownLatch(1);

    @KafkaListener(id = "message", topics = "payments")
    public void paymentListener(String message) {
        LOGGER.debug("received message:" + message);
        final Payment payment = new PaymentFactory().makePayment();
        applicationEventPublisher.publishEvent(new PaymentCreatedEvent(payment));
        this.latch1.countDown();
    }
}
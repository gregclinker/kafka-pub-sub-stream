package com.essexboy.kafka;

import org.springframework.context.ApplicationEvent;

public class PaymentCreatedEvent extends ApplicationEvent {

    public PaymentCreatedEvent(Payment source) {
        super(source);
    }
}
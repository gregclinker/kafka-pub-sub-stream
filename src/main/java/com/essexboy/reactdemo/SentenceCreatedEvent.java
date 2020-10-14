package com.essexboy.reactdemo;

import org.springframework.context.ApplicationEvent;

public class SentenceCreatedEvent extends ApplicationEvent {

    public SentenceCreatedEvent(Sentence source) {
        super(source);
    }
}
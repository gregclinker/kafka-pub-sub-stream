package com.essexboy.reactdemo;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
class SentenceCreatedEventPublisher implements ApplicationListener<SentenceCreatedEvent>, Consumer<FluxSink<SentenceCreatedEvent>> {

    private final Executor executor;
    private final BlockingQueue<SentenceCreatedEvent> queue =
            new LinkedBlockingQueue<>();

    SentenceCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(SentenceCreatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<SentenceCreatedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    SentenceCreatedEvent event = queue.take();
                    sink.next(event);
                } catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
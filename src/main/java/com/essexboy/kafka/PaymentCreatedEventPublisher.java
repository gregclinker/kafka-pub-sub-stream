package com.essexboy.kafka;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
class PaymentCreatedEventPublisher implements ApplicationListener<PaymentCreatedEvent>, Consumer<FluxSink<PaymentCreatedEvent>> {

    private final Executor executor;
    private final BlockingQueue<PaymentCreatedEvent> queue = new LinkedBlockingQueue<>();

    PaymentCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(PaymentCreatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<PaymentCreatedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    PaymentCreatedEvent event = queue.take();
                    sink.next(event);
                } catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
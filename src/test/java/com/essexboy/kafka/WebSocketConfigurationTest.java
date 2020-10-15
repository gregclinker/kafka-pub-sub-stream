package com.essexboy.kafka;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebSocketConfigurationTest {

    private final WebSocketClient socketClient = new ReactorNettyWebSocketClient();
    @Autowired
    private PaymentService paymentService;

    @Test
    public void testNotificationsOnUpdates() throws Exception {

        int count = 10;
        AtomicLong counter = new AtomicLong();
        URI uri = URI.create("ws://localhost:8080/ws/payments");

        socketClient.execute(uri, (WebSocketSession session) -> {

            Mono<WebSocketMessage> out = Mono.just(session.textMessage("test"));

            Flux<String> in = session
                    .receive()
                    .map(WebSocketMessage::getPayloadAsText);

            return session
                    .send(out)
                    .thenMany(in)
                    .doOnNext(str -> counter.incrementAndGet())
                    .then();

        }).subscribe();

        Flux
                .<Payment>generate(sink -> sink.next(paymentService.streamTestPayment()))
                .take(count)
                .blockLast();

        Thread.sleep(1000);

        Assertions.assertThat(counter.get()).isGreaterThanOrEqualTo(count);
    }
}
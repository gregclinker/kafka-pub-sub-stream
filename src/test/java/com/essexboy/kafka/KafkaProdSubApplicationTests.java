package com.essexboy.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class KafkaProdSubApplicationTests {

    @Autowired
    private Listener listener;

    @Autowired
    private KafkaTemplate<Integer, Payment> template;

    @Autowired
    private PaymentService paymentService;

    @Test
    public void testSimple() throws Exception {
        template.send("payments", new Random().nextInt(), new PaymentFactory().makePayment());
        template.flush();
        assertTrue(listener.latch1.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void paymentService() throws Exception {
        paymentService.streamTestPayment();
        assertTrue(listener.latch1.await(10, TimeUnit.SECONDS));
    }
}

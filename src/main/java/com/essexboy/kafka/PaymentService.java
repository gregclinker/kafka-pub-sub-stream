package com.essexboy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private Listener listener;

    @Autowired
    private KafkaTemplate<Integer, String> template;

    private Integer id = 0;

    public Payment streamTestPayment() {
        try {
            final Payment payment = new PaymentFactory().makePayment();
            template.send("payments", ++id, payment.toString());
            template.flush();
            return payment;
        } catch (Exception e) {
            LOGGER.error("error making test payment", e);
            throw e;
        }
    }
}

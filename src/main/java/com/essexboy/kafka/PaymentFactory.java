package com.essexboy.kafka;

import java.util.Random;

public class PaymentFactory {
    private static final Random RANDOM = new Random();

    public static Payment makePayment() {
        Payment payment = new Payment();
        payment.setId(RANDOM.nextInt());
        payment.setAccount(RANDOM.nextInt(10000000) + "");
        payment.setSort(RANDOM.nextInt(10) + "-" + RANDOM.nextInt(10) + "-" + RANDOM.nextInt(10));
        payment.setAmount(RANDOM.nextInt());
        return payment;
    }
}

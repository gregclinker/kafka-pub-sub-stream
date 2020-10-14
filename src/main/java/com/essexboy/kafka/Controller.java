package com.essexboy.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/testPayment")
    public ResponseEntity<Payment> makePayment() {
        return new ResponseEntity<Payment>(paymentService.streamTestPayment(), HttpStatus.OK);
    }
}

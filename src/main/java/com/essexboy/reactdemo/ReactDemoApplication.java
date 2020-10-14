package com.essexboy.reactdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReactDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactDemoApplication.class, args);
    }
}

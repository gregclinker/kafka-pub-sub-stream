package com.essexboy.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Payment {
    private Integer id;
    private String account;
    private String sort;
    private Integer amount;
}

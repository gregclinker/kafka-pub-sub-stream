package com.essexboy.reactdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Sentence {

    @Id
    private String id;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        System.out.println(text);
        this.text = text;
    }
}
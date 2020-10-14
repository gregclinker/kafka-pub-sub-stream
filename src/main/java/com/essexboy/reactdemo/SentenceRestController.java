package com.essexboy.reactdemo;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@org.springframework.context.annotation.Profile("classic")
class SentenceRestController {

    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
    private final SentenceService sentenceService;

    SentenceRestController(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @GetMapping
    Publisher<Sentence> getAll() {
        return this.sentenceService.all();
    }

    @GetMapping("/{id}")
    Publisher<Sentence> getById(@PathVariable("id") String id) {
        return this.sentenceService.get(id);
    }

    @PostMapping
    Publisher<ResponseEntity<Sentence>> create(@RequestBody Sentence sentence) {
        return this.sentenceService
                .create(sentence.getText())
                .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
                        .contentType(mediaType)
                        .build());
    }

    @DeleteMapping("/{id}")
    Publisher<Sentence> deleteById(@PathVariable String id) {
        return this.sentenceService.delete(id);
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<Sentence>> updateById(@PathVariable String id, @RequestBody Sentence sentence) {
        return Mono
                .just(sentence)
                .flatMap(p -> this.sentenceService.update(id, p.getText()))
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(this.mediaType)
                        .build());
    }
}
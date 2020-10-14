package com.essexboy.reactdemo;

import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Log4j2
@Component
class SentenceHandler {

    private final SentenceService sentenceService;

    SentenceHandler(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Sentence> profiles) {
        return Mono
                .from(profiles)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/profiles/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<Sentence> profiles) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(profiles, Sentence.class);
    }

    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }

    Mono<ServerResponse> getById(ServerRequest r) {
        return defaultReadResponse(this.sentenceService.get(id(r)));
    }

    Mono<ServerResponse> all(ServerRequest r) {
        return defaultReadResponse(this.sentenceService.all());
    }

    Mono<ServerResponse> deleteById(ServerRequest r) {
        return defaultReadResponse(this.sentenceService.delete(id(r)));
    }

    Mono<ServerResponse> deleteAll(ServerRequest r) {
        final Mono<Void> voidMono = this.sentenceService.deleteAll();
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(voidMono, Void.class);
    }

    Mono<ServerResponse> updateById(ServerRequest r) {
        Flux<Sentence> id = r.bodyToFlux(Sentence.class)
                .flatMap(p -> this.sentenceService.update(id(r), p.getText()));
        return defaultReadResponse(id);
    }

    Mono<ServerResponse> create(ServerRequest request) {
        Flux<Sentence> flux = request
                .bodyToFlux(Sentence.class)
                .flatMap(toWrite -> this.sentenceService.create(toWrite.getText()));
        return defaultWriteResponse(flux);
    }
}
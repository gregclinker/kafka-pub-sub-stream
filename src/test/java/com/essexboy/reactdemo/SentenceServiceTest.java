package com.essexboy.reactdemo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.function.Predicate;

@Log4j2
@DataMongoTest
@Import(SentenceService.class)
@ActiveProfiles("test")
public class SentenceServiceTest {

    private final SentenceService service;
    private final SentenceRepository repository;

    public SentenceServiceTest(@Autowired SentenceService service, @Autowired SentenceRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Test
    public void getAll() {
        repository.deleteAll();
        Flux<Sentence> saved = repository.saveAll(Flux.just(new Sentence(null, "Josh"), new Sentence(null, "Matt"), new Sentence(null, "Jane")));

        Flux<Sentence> composite = service.all().thenMany(saved);

        Predicate<Sentence> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }

    @Test
    public void save() {
        Mono<Sentence> profileMono = this.service.create("email@email.com");
        StepVerifier
                .create(profileMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void delete() {
        String test = "test";
        Mono<Sentence> deleted = this.service
                .create(test)
                .flatMap(saved -> this.service.delete(saved.getId()));
        StepVerifier
                .create(deleted)
                .expectNextMatches(profile -> profile.getText().equalsIgnoreCase(test))
                .verifyComplete();
    }

    @Test
    public void deleteAll() {
        repository.deleteAll();
        Flux<Sentence> saved = repository.saveAll(Flux.just(new Sentence(null, "Josh"), new Sentence(null, "Matt"), new Sentence(null, "Jane")));

        Flux<Sentence> composite = service.all().thenMany(saved);

        Predicate<Sentence> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();

        final Mono<Void> voidMono = service.deleteAll();
        StepVerifier
                .create(voidMono)
                .verifyComplete();

        final Flux<Sentence> all = repository.findAll();
        StepVerifier
                .create(all)
                .verifyComplete();
    }

    @Test
    public void update() throws Exception {
        Mono<Sentence> saved = this.service
                .create("test")
                .flatMap(p -> this.service.update(p.getId(), "test1"));
        StepVerifier
                .create(saved)
                .expectNextMatches(p -> p.getText().equalsIgnoreCase("test1"))
                .verifyComplete();
    }

    @Test
    public void getById() {
        String test = UUID.randomUUID().toString();
        Mono<Sentence> deleted = this.service
                .create(test)
                .flatMap(saved -> this.service.get(saved.getId()));
        StepVerifier
                .create(deleted)
                .expectNextMatches(profile -> StringUtils.hasText(profile.getId()) && test.equalsIgnoreCase(profile.getText()))
                .verifyComplete();
    }
}

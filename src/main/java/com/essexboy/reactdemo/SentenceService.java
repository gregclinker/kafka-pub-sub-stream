package com.essexboy.reactdemo;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
class SentenceService {

    private final ApplicationEventPublisher publisher;
    private final SentenceRepository sentenceRepository;

    SentenceService(ApplicationEventPublisher publisher, SentenceRepository sentenceRepository) {
        this.publisher = publisher;
        this.sentenceRepository = sentenceRepository;
    }

    public Flux<Sentence> all() {
        return this.sentenceRepository.findAll();
    }

    public Mono<Sentence> get(String id) {
        return this.sentenceRepository.findById(id);
    }

    public Mono<Sentence> update(String id, String text) {
        return this.sentenceRepository
                .findById(id)
                .map(p -> new Sentence(p.getId(), text))
                .flatMap(this.sentenceRepository::save);
    }

    public Mono<Sentence> delete(String id) {
        return this.sentenceRepository
                .findById(id)
                .flatMap(p -> this.sentenceRepository.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<Void> deleteAll() {
        return this.sentenceRepository.deleteAll();
    }

    public Mono<Sentence> create(String text) {
        return this.sentenceRepository
                .save(new Sentence(null, text))
                .doOnSuccess(sentence -> this.publisher.publishEvent(new SentenceCreatedEvent(sentence)));
    }
}
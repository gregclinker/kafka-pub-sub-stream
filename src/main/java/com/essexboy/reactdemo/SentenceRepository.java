package com.essexboy.reactdemo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface SentenceRepository extends ReactiveMongoRepository<Sentence, String> {
}
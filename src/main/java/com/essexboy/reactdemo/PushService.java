package com.essexboy.reactdemo;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

@Log4j2
@Service
@Profile("default")
class PushService {

    private Deque<String> sentencesStrings;
    private SentenceService sentenceService;

    PushService(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @PostConstruct
    public void init() throws Exception {
        final Mono<Void> voidMono = sentenceService.deleteAll();
        final InputStream inputStream = getClass().getResourceAsStream("/war_and_peace.txt");
        final String content = IOUtils.toString(inputStream, Charset.defaultCharset());
        sentencesStrings = new ArrayDeque<>(Arrays.asList(content.split("\\.")));
    }

    @Scheduled(fixedRate = 2000, initialDelay = 30000)
    public void push() {
        if (!sentencesStrings.isEmpty()) {
            final String sentence = sentencesStrings.removeFirst();
            sentenceService.create(sentence).block();
        }
    }
}
package com.essexboy.reactdemo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@Log4j2
@ActiveProfiles("default")
@Import({SentenceEndpointConfiguration.class,
        SentenceHandler.class, SentenceService.class})
public class FunctionalSentenceEndpointsTest extends AbstractBaseSentenceEndpoints {

    FunctionalSentenceEndpointsTest(@Autowired WebTestClient client) {
        super(client);
    }

    @BeforeAll
    static void before() {
        log.info("running default " + SentenceRestController.class.getName() + " tests");
    }
}
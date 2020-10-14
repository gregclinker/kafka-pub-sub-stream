package com.essexboy.reactdemo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@Log4j2
@Import({SentenceRestController.class, SentenceService.class})
@ActiveProfiles("classic")
public class ClassicSentenceEndpointsTest extends AbstractBaseSentenceEndpoints {

    ClassicSentenceEndpointsTest(@Autowired WebTestClient client) {
        super(client);
    }

    @BeforeAll
    static void before() {
        log.info("running non-classic tests");
    }
}
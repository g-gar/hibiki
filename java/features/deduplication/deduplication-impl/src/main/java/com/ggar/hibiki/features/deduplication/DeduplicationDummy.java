package com.ggar.hibiki.features.deduplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeduplicationDummy {
    public void hello() {
        log.info("Hello from Deduplication!");
    }
}

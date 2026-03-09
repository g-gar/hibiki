package com.ggar.hibiki.features.entitlements;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EntitlementsDummy {
    public void hello() {
        log.info("Hello from Entitlements!");
    }
}

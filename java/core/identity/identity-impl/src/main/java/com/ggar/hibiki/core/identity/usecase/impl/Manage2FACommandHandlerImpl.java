package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.dto.TwoFactorRequest;
import com.ggar.hibiki.core.identity.service.Manage2FACommandHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class Manage2FACommandHandlerImpl implements Manage2FACommandHandler {

    @Override
    public Mono<Void> handle(TwoFactorRequest request) {
        // TODO: Implement 2FA management logic
        return Mono.error(new RuntimeException("Manage2FA not implemented yet"));
    }
}

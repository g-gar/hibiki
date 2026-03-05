package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.model.UpdateProfileRequest;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.usecase.UpdateProfileCommandHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProfileCommandHandlerImpl implements UpdateProfileCommandHandler {

    @Override
    public Mono<User> handle(UpdateProfileRequest request) {
        // TODO: Implement profile update logic
        return Mono.error(new RuntimeException("UpdateProfile not implemented yet"));
    }
}

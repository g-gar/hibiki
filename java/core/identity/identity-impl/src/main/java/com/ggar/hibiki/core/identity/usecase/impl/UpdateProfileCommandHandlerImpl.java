package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.dto.UpdateProfileRequest;
import com.ggar.hibiki.core.identity.dto.UserDto;
import com.ggar.hibiki.core.identity.service.UpdateProfileCommandHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProfileCommandHandlerImpl implements UpdateProfileCommandHandler {

    @Override
    public Mono<UserDto> handle(UpdateProfileRequest request) {
        // TODO: Implement profile update logic
        return Mono.error(new RuntimeException("UpdateProfile not implemented yet"));
    }
}

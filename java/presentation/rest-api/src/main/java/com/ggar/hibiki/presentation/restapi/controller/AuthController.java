package com.ggar.hibiki.presentation.restapi.controller;

import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.presentation.restapi.mapper.AuthMapper;
import com.ggar.hibiki.presentation.restapi.model.LoginRequestDTO;
import com.ggar.hibiki.presentation.restapi.model.SignupRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Mediator mediator;
    private final AuthMapper authMapper;

    public AuthController(Mediator mediator, AuthMapper authMapper) {
        this.mediator = mediator;
        this.authMapper = authMapper;
    }

    @PostMapping("/signup")
    public Mono<Void> signup(@RequestBody SignupRequestDTO request) {
        return mediator.send(authMapper.toDomain(request));
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody LoginRequestDTO request) {
        return mediator.send(authMapper.toDomain(request));
    }
}

package com.ggar.hibiki.presentation.restapi.controller;

import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.presentation.restapi.mapper.AuthMapper;
import com.ggar.hibiki.core.orchestrator.auth.model.LoginRequestDTO;
import com.ggar.hibiki.core.orchestrator.auth.model.SignupRequestDTO;
import com.ggar.hibiki.core.orchestrator.auth.model.RefreshRequestDTO;
import com.ggar.hibiki.core.orchestrator.auth.LoginUseCase;
import com.ggar.hibiki.core.orchestrator.auth.RefreshAuthUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Mediator mediator;
    private final AuthMapper authMapper;
    private final LoginUseCase loginUseCase;
    private final RefreshAuthUseCase refreshAuthUseCase;

    public AuthController(Mediator mediator, AuthMapper authMapper,
            LoginUseCase loginUseCase, RefreshAuthUseCase refreshAuthUseCase) {
        this.mediator = mediator;
        this.authMapper = authMapper;
        this.loginUseCase = loginUseCase;
        this.refreshAuthUseCase = refreshAuthUseCase;
    }

    @PostMapping("/signup")
    public Mono<Void> signup(@RequestBody SignupRequestDTO request) {
        return mediator.send(authMapper.toDomain(request));
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(
            @RequestBody LoginRequestDTO request,
            @RequestHeader(value = "X-Forwarded-For", defaultValue = "unknown") String ip,
            @RequestHeader(value = "User-Agent", defaultValue = "unknown") String userAgent) {
        return loginUseCase.execute(request, ip, userAgent);
    }

    @PostMapping("/refresh")
    public Mono<AuthResponse> refresh(
            @RequestBody RefreshRequestDTO request,
            @RequestHeader(value = "X-Forwarded-For", defaultValue = "unknown") String ip,
            @RequestHeader(value = "User-Agent", defaultValue = "unknown") String userAgent) {
        return refreshAuthUseCase.execute(request, ip, userAgent);
    }
}

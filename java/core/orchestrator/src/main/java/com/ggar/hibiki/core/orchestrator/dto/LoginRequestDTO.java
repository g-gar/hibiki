package com.ggar.hibiki.core.orchestrator.dto;

import java.util.UUID;
import lombok.Value;

@Value
public class LoginRequestDTO {
    String username;
    String password;
    UUID deviceId;
}

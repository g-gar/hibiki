package com.ggar.hibiki.core.orchestrator.auth.model;

import java.util.UUID;
import lombok.Value;

@Value
public class RefreshRequestDTO {
    String refreshToken;
    UUID deviceId;
}

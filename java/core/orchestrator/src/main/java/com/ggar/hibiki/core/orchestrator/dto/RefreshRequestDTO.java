package com.ggar.hibiki.core.orchestrator.dto;

import java.util.UUID;
import lombok.Value;

@Value
public class RefreshRequestDTO {
    String refreshToken;
    UUID deviceId;
}

package com.ggar.hibiki.core.orchestrator.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequestDTO {
    private String refreshToken;
    private String deviceId;
}

package com.ggar.hibiki.core.identity.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Command<AuthResponse> {
    private String username;
    private String password;

    // Device Tracking Info
    private String deviceId;
    private String ip;
    private String userAgent;
}

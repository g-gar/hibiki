package com.ggar.hibiki.core.identity.dto;

import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshAuthRequest implements Command<AuthResponse> {
    private String refreshToken;
    private String deviceId;
}

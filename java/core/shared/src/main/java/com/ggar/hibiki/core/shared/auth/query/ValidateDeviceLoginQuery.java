package com.ggar.hibiki.core.shared.auth.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ValidateDeviceLoginQuery implements Query<Boolean> {
    UUID userId;
    UUID deviceId;
    String ip;
    String userAgent;
}

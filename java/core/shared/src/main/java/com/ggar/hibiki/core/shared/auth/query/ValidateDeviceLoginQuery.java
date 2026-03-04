package com.ggar.hibiki.core.shared.auth.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ValidateDeviceLoginQuery implements Query<Boolean> {
    String userId;
    String deviceId;
    String ip;
    String userAgent;
}

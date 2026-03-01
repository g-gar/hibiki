package com.ggar.hibiki.core.shared.event.domain;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import lombok.Value;

@Value
public class UserSignedUpEvent implements DomainEvent {
    String userId;
    String email;
}

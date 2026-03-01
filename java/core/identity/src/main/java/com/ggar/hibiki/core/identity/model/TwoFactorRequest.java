package com.ggar.hibiki.core.identity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ggar.hibiki.core.shared.mediator.Command;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorRequest implements Command<Void> {
    private String userId;
    private String code;
    private boolean enabled;
}

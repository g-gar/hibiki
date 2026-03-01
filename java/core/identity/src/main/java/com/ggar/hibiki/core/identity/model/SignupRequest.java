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
public class SignupRequest implements Command<Void> {
    private String username;
    private String email;
    private String password;
}

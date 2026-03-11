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
public class UpdateProfileRequest implements Command<UserDto> {
    private String userId;
    private String username;
    private String email;
    private String password;
}

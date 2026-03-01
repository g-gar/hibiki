package com.ggar.hibiki.core.identity.model;

import com.ggar.hibiki.core.shared.mediator.Query;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenQuery implements Query<Claims> {
    private String token;
}

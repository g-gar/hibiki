package com.ggar.hibiki.core.identity.model;

import java.util.UUID;

/**
 * Authentication context containing tokens.
 */
public record AuthContext(UUID id, String accessToken, String refreshToken) {}

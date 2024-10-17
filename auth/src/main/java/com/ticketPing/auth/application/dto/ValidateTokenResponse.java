package com.ticketPing.auth.application.dto;

import java.util.UUID;

public record ValidateTokenResponse(
    UUID userId,
    String role
) { }

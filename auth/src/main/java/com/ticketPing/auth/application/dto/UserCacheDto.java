package com.ticketPing.auth.application.dto;

import java.util.UUID;

public record UserCacheDto(
    UUID userId,
    String role
) { }

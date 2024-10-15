package com.ticketPing.order.application.dtos.temp;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
    UUID userId,
    String email,
    String nickname,
    LocalDate birthday,
    String gender
) {

}

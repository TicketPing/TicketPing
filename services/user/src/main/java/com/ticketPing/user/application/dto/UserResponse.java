package com.ticketPing.user.application.dto;
import com.ticketPing.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UserResponse(
        UUID userId,
        String email,
        String nickname,
        LocalDate birthday,
        String gender
) {
    public static UserResponse of(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birthday(user.getBirthday())
                .gender(user.getGender().getValue())
                .build();
    }
}

package response;
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
    // TODO: User로 받아오는 방법?
    public static UserResponse of(UUID userId, String email, String nickname,
                                  LocalDate birthday, String gender) {
        return UserResponse.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .birthday(birthday)
                .gender(gender)
                .build();
    }
}

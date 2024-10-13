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
) {}

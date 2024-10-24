package dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    private UUID scheduleId;
    private UUID seatId;
    private UUID userId;

    public static PaymentRequestDto field(UUID scheduleId, UUID seatId, UUID userId) {
        return PaymentRequestDto.builder()
                .scheduleId(scheduleId)
                .seatId(seatId)
                .userId(userId)
                .build();
    }
}

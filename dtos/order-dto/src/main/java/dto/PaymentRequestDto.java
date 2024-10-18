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

    public static PaymentRequestDto field(UUID scheduleId, UUID seatId) {
        return PaymentRequestDto.builder()
                .scheduleId(scheduleId)
                .seatId(seatId)
                .build();
    }
}

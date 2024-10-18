package dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    private String performanceName;
    private UUID scheduleId;
    private UUID seatId;

    public static PaymentRequestDto field(String performanceName, UUID scheduleId, UUID seatId) {
        return PaymentRequestDto.builder()
                .performanceName(performanceName)
                .scheduleId(scheduleId)
                .seatId(seatId)
                .build();
    }
}

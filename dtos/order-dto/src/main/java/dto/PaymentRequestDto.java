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
    private String seatInfo;

    public static PaymentRequestDto field(String performanceName, UUID scheduleId, String seatInfo) {
        return PaymentRequestDto.builder()
                .performanceName(performanceName)
                .scheduleId(scheduleId)
                .seatInfo(seatInfo)
                .build();
    }
}

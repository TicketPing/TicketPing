package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private String performanceName;
    private UUID performanceScheduleId;
    private String seatInfo;
    private Long amount;
    private UUID userId;
}

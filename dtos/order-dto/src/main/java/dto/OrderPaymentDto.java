package dto;

import java.util.UUID;

public record OrderPaymentDto(
    UUID userId,
    UUID orderId
) {

}

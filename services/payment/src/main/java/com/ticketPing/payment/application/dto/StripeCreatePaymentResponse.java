package com.ticketPing.payment.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StripeCreatePaymentResponse {

    private String clientSecret;
    private String dpmCheckerLink;
    public StripeCreatePaymentResponse(String clientSecret, String transactionId) {
        this.clientSecret = clientSecret;
        // [DEV]: For demo purposes only, you should avoid exposing the PaymentIntent ID in the client-side code.
        this.dpmCheckerLink = "https://dashboard.stripe.com/settings/payment_methods/review?transaction_id="+transactionId;
    }
}


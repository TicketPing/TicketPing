package com.ticketPing.payment.presentation.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    private static final String endpointSecret = "whsec_tNetAhqw4xyPG6PSNyRaaocG6M0xKloc";

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            return "webhook Error : " + e.getMessage();
        }

        //payment success
        if("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
            System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
        }

        // PaymentIntent 실패 이벤트 처리
        if ("payment_intent.payment_failed".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
            // 결제 실패 처리 로직 추가
            System.out.println("Payment failed.");
        }

        return "Success";
    }
}

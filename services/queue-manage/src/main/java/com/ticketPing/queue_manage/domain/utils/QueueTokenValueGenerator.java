package com.ticketPing.queue_manage.domain.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class QueueTokenValueGenerator {

    private static final String SECRET_KEY = "secret-key";

    public static String generateTokenValue(String userId, String performanceId) {
        try {
            String input = userId + SECRET_KEY;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return "Token:" + performanceId + ":" + Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

}

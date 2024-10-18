package com.ticketping.gateway.domain.utils;

import static com.ticketping.gateway.infrastructure.enums.RedisKeyPrefix.TOKEN_VALUE;
import static com.ticketping.gateway.infrastructure.utils.ConfigHolder.tokenValueSecretKey;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class QueueTokenValueGenerator {

    public static String generateTokenValue(String userId, String performanceId) {
        try {
            String input = userId + tokenValueSecretKey();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return TOKEN_VALUE.getValue() + performanceId + ":" + Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

}

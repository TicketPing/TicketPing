package com.ticketping.gateway.infrastructure.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 환경 설정
 */
@Component
public class ConfigHolder {

    private static String tokenValueSecretKey;

    @Value("${token-value.secret-key}")
    public void setSecretKey(String secretKey) {
        tokenValueSecretKey = secretKey;
    }

    public static String tokenValueSecretKey() {
        return tokenValueSecretKey;
    }

}
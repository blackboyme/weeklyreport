package com.caac.weeklyreport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenConfig {
    private String secretKey;
    private long validityMinutes;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getValidityMinutes() {
        return validityMinutes;
    }

    public void setValidityMinutes(long validityMinutes) {
        this.validityMinutes = validityMinutes;
    }
} 
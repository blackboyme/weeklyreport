package com.caac.weeklyreport.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.time.Instant;

public class TokenUtil {
    private static final String SECRET_KEY = "YourSecretKey123"; // 请更改为您的密钥
    private static final String ALGORITHM = "AES";
    private static final long TOKEN_VALIDITY = 7 * 24 * 60; // Token有效期一周

    public static String generateToken(String userId) {
        try {
            long expirationTime = Instant.now().getEpochSecond() + TOKEN_VALIDITY;
            String data = userId + ":" + expirationTime;
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public static String validateToken(String token) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(token)));
            String[] parts = decryptedData.split(":");
            long expirationTime = Long.parseLong(parts[1]);
            if (Instant.now().getEpochSecond() > expirationTime) {
                return null; // Token已过期
            }
            return parts[0]; // 返回用户ID
        } catch (Exception e) {
            return null; // 如果token无效或过期，返回null
        }
    }

    public static boolean isTokenExpiringSoon(String token) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(token)));
            String[] parts = decryptedData.split(":");
            long expirationTime = Long.parseLong(parts[1]);
            // 如果token将在5分钟内过期，返回true
            return (expirationTime - Instant.now().getEpochSecond()) < 300;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUserIdFromToken(String token) {
        return validateToken(token);
    }
}

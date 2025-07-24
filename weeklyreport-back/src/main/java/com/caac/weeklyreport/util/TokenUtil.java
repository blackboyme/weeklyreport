package com.caac.weeklyreport.util;

import com.caac.weeklyreport.biz.user.entity.UserInfo;
import com.caac.weeklyreport.config.TokenConfig;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class TokenUtil {
    private static TokenConfig tokenConfig;

    public TokenUtil(TokenConfig tokenConfig) {
        TokenUtil.tokenConfig = tokenConfig;
    }

    private static final String ALGORITHM = "AES";

    public static String generateToken(UserInfo userInfo) {
        try {
            // 更新token和更新时间
            userInfo.setToken(null); // 避免token递归
            userInfo.setUpdatedAt(LocalDateTime.now());
            
            long expirationTime = Instant.now().getEpochSecond() + (tokenConfig.getValidityMinutes() * 60);
            String jsonData = userInfo.toJsonString();
            String data = jsonData + ":" + expirationTime;
            
            SecretKeySpec secretKey = new SecretKeySpec(tokenConfig.getSecretKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String token = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
            
            // 设置生成的token
            userInfo.setToken(token);
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public static UserInfo validateToken(String token) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(tokenConfig.getSecretKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(token)));
            
            // 分离JSON数据和过期时间
            int lastColonIndex = decryptedData.lastIndexOf(':');
            if (lastColonIndex == -1) {
                return null;
            }
            
            String jsonData = decryptedData.substring(0, lastColonIndex);
            long expirationTime = Long.parseLong(decryptedData.substring(lastColonIndex + 1));
            
            if (Instant.now().getEpochSecond() > expirationTime) {
                return null; // Token已过期
            }
            
            UserInfo userInfo = UserInfo.fromJsonString(jsonData);
            userInfo.setToken(token);
            return userInfo;
        } catch (Exception e) {
            return null; // 如果token无效或过期，返回null
        }
    }

    public static boolean isTokenExpiringSoon(String token) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(tokenConfig.getSecretKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(token)));
            
            int lastColonIndex = decryptedData.lastIndexOf(':');
            if (lastColonIndex == -1) {
                return false;
            }
            
            long expirationTime = Long.parseLong(decryptedData.substring(lastColonIndex + 1));
            // 如果token将在5分钟内过期，返回true
            return (expirationTime - Instant.now().getEpochSecond()) < 300;
        } catch (Exception e) {
            return false;
        }
    }

    public static UserInfo getUserInfoFromToken(String token) {
        return validateToken(token);
    }
}

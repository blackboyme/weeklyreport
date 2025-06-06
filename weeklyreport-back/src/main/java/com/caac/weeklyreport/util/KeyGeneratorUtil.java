package com.caac.weeklyreport.util;

import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.exception.BusinessException;

import java.util.UUID;

public class KeyGeneratorUtil {
    /**
     * 随机生成UUID
     *
     * @return uuid
     * @since 2019年8月19日
     */
    public static String generateUUID() {
        try {
//			String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            UUID uuid = UUID.randomUUID();
            return uuid.toString().replace("-", "");
        } catch (Exception e) {
            throw new BusinessException(ResultCode.ERROR);
        }
    }
}

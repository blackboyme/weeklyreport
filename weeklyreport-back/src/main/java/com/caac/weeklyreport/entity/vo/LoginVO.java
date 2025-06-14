package com.caac.weeklyreport.entity.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String encryptedData;
    private String iv;
    private String code;
}

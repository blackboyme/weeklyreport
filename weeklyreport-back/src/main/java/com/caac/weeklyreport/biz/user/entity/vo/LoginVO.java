package com.caac.weeklyreport.biz.user.entity.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String encryptedData;
    private String iv;
    private String code;
}

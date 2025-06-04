package com.caac.weeklyreport.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private String id;
    private String name;
    private String nickname;
    private String password;
    private String token;
    private String deleted;
    private String note;  // 新增的 note 字段
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

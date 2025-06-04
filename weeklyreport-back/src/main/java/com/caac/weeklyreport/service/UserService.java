package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    User createUser(User user);
    User getUserById(String id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(String id);
    User login(String phoneNo);
    User validateUserByToken(String token);
    String refreshToken(String oldToken); // 新增方法
}

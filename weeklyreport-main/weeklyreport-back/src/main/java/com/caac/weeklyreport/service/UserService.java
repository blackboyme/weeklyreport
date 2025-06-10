package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.User;
import com.caac.weeklyreport.entity.UserInfo;

import java.util.List;

public interface UserService extends IService<User> {
    User createUser(User user);
    User getUserById(String id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(String id);
    UserInfo login(String phoneNo);
    UserInfo validateUserByToken(String token);
    String refreshToken(String oldToken);
}

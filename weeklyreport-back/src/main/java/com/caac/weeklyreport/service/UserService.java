package com.caac.weeklyreport.service;

import com.caac.weeklyreport.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(String id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(String id);
    User login(String name, String password);
    User validateUserByToken(String token);
    String refreshToken(String oldToken); // 新增方法
}

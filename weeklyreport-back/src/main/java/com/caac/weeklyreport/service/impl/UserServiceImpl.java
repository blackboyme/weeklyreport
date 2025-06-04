package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caac.weeklyreport.entity.User;
import com.caac.weeklyreport.mapper.UserMapper;
import com.caac.weeklyreport.service.UserService;
import com.caac.weeklyreport.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setDeleted("0");
        userMapper.insert(user);
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", "0");
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User updateUser(User user) {
        userMapper.updateById(user);
        return getUserById(user.getId());
    }

    @Override
    public void deleteUser(String id) {
        User user = new User();
        user.setId(id);
        user.setDeleted("1");
        userMapper.updateById(user);
    }

    @Override
    public User login(String name, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name)
                    .eq("password", password)
                    .eq("deleted", "0");
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            // 使用新的token生成方法
            String newToken = TokenUtil.generateToken(user.getId());
            user.setToken(newToken);
            userMapper.updateById(user);
            return user;
        }
        return null;
    }

    // 添加一个新方法来验证token
    @Override
    public User validateUserByToken(String token) {
        String userId = TokenUtil.validateToken(token);
        if (userId != null) {
            User user = getUserById(userId);
            if (user != null && TokenUtil.isTokenExpiringSoon(token)) {
                // 如果token即将过期，生成新的token
                String newToken = TokenUtil.generateToken(userId);
                user.setToken(newToken);
                userMapper.updateById(user);
            }
            return user;
        }
        return null;
    }

    @Override
    public String refreshToken(String oldToken) {
        String userId = TokenUtil.validateToken(oldToken);
        if (userId != null) {
            String newToken = TokenUtil.generateToken(userId);
            User user = getUserById(userId);
            user.setToken(newToken);
            userMapper.updateById(user);
            return newToken;
        }
        return null;
    }
}

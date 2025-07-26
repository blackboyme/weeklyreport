package com.caac.weeklyreport.biz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.user.entity.User;
import com.caac.weeklyreport.biz.user.entity.UserInfo;
import com.caac.weeklyreport.biz.user.mapper.UserMapper;
import com.caac.weeklyreport.biz.user.service.UserService;
import com.caac.weeklyreport.util.KeyGeneratorUtil;
import com.caac.weeklyreport.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(User user) {
        user.setUserId(KeyGeneratorUtil.generateUUID());
        user.setIsDeleted("0");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);
        return getUserById(user.getUserId());
    }

    @Override
    public User getUserById(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User updateUser(User user) {
        userMapper.updateById(user);
        return getUserById(user.getUserId());
    }

    @Override
    public void deleteUser(String id) {
        User user = new User();
        user.setUserId(id);
        user.setIsDeleted("1");
        userMapper.updateById(user);
    }

    @Override
    public UserInfo login(String phoneNo,String openId) {
        // 使用新的查询方法获取完整的用户信息
        UserInfo userInfo = userMapper.getUserInfoByPhoneNo(phoneNo);
        if (userInfo != null) {
            // 生成新的token
            String newToken = TokenUtil.generateToken(userInfo);
            // 更新用户token
            User user = userMapper.selectById(userInfo.getUserId());
            user.setUserId(userInfo.getUserId());
            user.setToken(newToken);
            if(!StringUtils.isEmpty(openId)){
                user.setOpenId(openId);
            }
            userMapper.updateById(user);
            return userMapper.getUserInfoByPhoneNo(phoneNo);
        }
        return null;
    }

    // 添加一个新方法来验证token
    @Override
    public UserInfo validateUserByToken(String token) {
        UserInfo userInfo = TokenUtil.validateToken(token);
        if (userInfo != null) {
            // 重新从数据库获取最新的用户信息
            UserInfo latestUserInfo = userMapper.getUserInfoByPhoneNo(userInfo.getPhoneNo());
            if (latestUserInfo != null) {
                if (TokenUtil.isTokenExpiringSoon(token)) {
                    // 如果token即将过期，生成新的token
                    String newToken = TokenUtil.generateToken(latestUserInfo);
                    User user = new User();
                    user.setUserId(latestUserInfo.getUserId());
                    user.setToken(newToken);
                    userMapper.updateById(user);
                    latestUserInfo.setToken(newToken);
                } else {
                    latestUserInfo.setToken(token);
                }
                return latestUserInfo;
            }
        }
        return null;
    }

    @Override
    public String refreshToken(String oldToken) {
        UserInfo userInfo = TokenUtil.validateToken(oldToken);
        if (userInfo != null) {
            UserInfo latestUserInfo = userMapper.getUserInfoByPhoneNo(userInfo.getPhoneNo());
            if (latestUserInfo != null) {
                String newToken = TokenUtil.generateToken(latestUserInfo);
                User user = new User();
                user.setUserId(latestUserInfo.getUserId());
                user.setToken(newToken);
                userMapper.updateById(user);
                return newToken;
            }
        }
        return null;
    }
}

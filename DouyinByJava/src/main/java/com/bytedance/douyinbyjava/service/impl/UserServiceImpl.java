package com.bytedance.douyinbyjava.service.impl;

import com.bytedance.douyinbyjava.entity.UserInfo;
import com.bytedance.douyinbyjava.mapper.UserMapper;
import com.bytedance.douyinbyjava.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    @Override
    public int insertUser(UserInfo userInfo) {
        return userMapper.insertUser(userInfo);
    }

    @Override
    public UserInfo getUser(String username) {
        return userMapper.getUser(username);
    }

    @Override
    public UserInfo getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public UserInfo getUserInfo(Integer user_id, Integer guest_id) {
        return userMapper.getUserInfo(user_id, guest_id);
    }
}

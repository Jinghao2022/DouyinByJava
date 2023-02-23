package com.bytedance.douyinbyjava.service;

import com.bytedance.douyinbyjava.entity.UserInfo;

public interface UserService {
    int insertUser(UserInfo userInfo);

    UserInfo getUser(String username);

    UserInfo getUserById(int userId);

    UserInfo getUserInfo(Integer user_id, Integer guest_id);
}

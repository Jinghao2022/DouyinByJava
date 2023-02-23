package com.bytedance.douyinbyjava.controller;

import com.bytedance.douyinbyjava.entity.UserInfo;
import com.bytedance.douyinbyjava.response.ResponseDto;
import com.bytedance.douyinbyjava.service.UserService;
import com.bytedance.douyinbyjava.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/douyin/user")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/register")
    public ResponseDto<String> register (String username, String password) {
        if (username == null) {
            return ResponseDto.failure("注册用户的用户名为空");
        }
        if (userService.getUser(username) != null) {
            return ResponseDto.failure("该用户名已存在");
        }

        if (password == null) {
            return ResponseDto.failure("注册用户的密码为空");
        }

        String passwordSecurity = DigestUtils.md5DigestAsHex(password.getBytes());
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(passwordSecurity);
        userService.insertUser(user);
        Integer user_id = user.getUserId();
        String token = TokenUtils.token(username, password, user_id.toString());
        return ResponseDto.success(token).add("user_id", user_id);
    }

    @PostMapping("/login")
    public ResponseDto<String> login (HttpServletRequest request, String username, String password) {
        String passwordSecurity = DigestUtils.md5DigestAsHex(password.getBytes());
        UserInfo user = userService.getUser(username);

        if (user == null) {
            return ResponseDto.failure("用户名不存在！");
        }

        if (!user.getPassword().equals(passwordSecurity)) {
            return ResponseDto.failure("密码错误！");
        }

        String token = TokenUtils.token(username, password, user.getUserId().toString());
        request.getSession().setAttribute("user", token);
        return ResponseDto.success(token);
    }

    @GetMapping("")
    public ResponseDto<UserInfo> user (String token, String user_id) {
        UserInfo userInfo = null;
        if (user_id == null) return ResponseDto.failure("用户不存在！");
        int userId = Integer.parseInt(user_id);

        if (!TokenUtils.verify(token)) {
            return ResponseDto.failure("用户未登录！");
        }
        String guestId = TokenUtils.parseId(token);

        String key = "user_" + user_id + "_" + guestId;
        userInfo = (UserInfo) redisTemplate.opsForValue().get(key);
        if (userInfo != null) {
            return ResponseDto.success(userInfo);
        }

        userInfo = userService.getUserInfo(userId, Integer.parseInt(guestId));
        if (userInfo == null) {
            return ResponseDto.failure("用户不存在！");
        }

        redisTemplate.opsForValue().set(key, userInfo, 60, TimeUnit.MINUTES);
        return ResponseDto.success(userInfo);
    }
}

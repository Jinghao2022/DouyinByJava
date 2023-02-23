package com.bytedance.douyinbyjava.controller;

import com.bytedance.douyinbyjava.entity.Video;
import com.bytedance.douyinbyjava.response.ResponseDto;
import com.bytedance.douyinbyjava.service.FavoriteService;
import com.bytedance.douyinbyjava.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/douyin/favorite")
public class FavoriteController {
    @Resource
    FavoriteService favoriteService;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${douyin.imagePath}")
    private String imagePath;

    @Value("${douyin.videoPath}")
    private String videoPath;

    @PostMapping("/action")
    public ResponseDto<String> action (String token, String videoId, String actionType) {
        if (!TokenUtils.verify(token)) return ResponseDto.failure("用户登录错误");
        if (videoId == null) return ResponseDto.failure("视频不正确！");
        int video_id = Integer.parseInt(videoId);
        if (video_id <= 0) return ResponseDto.failure("视频号不正确！");
        if (actionType == null) return ResponseDto.failure("用户行为不正确！");

        int action_type = Integer.parseInt(actionType);
        int userId = Integer.parseInt(TokenUtils.parseId(token));

        String key = "favorite_" + userId + "_" + userId;

        if (action_type == 1) {
            int res = favoriteService.insertFavorite(video_id, userId);
            if (res <= 0) return ResponseDto.failure("点赞失败，请重试！");
            else {
                redisTemplate.delete(key);
                return ResponseDto.success("点赞成功！");
            }
        } else if (action_type == 2) {
            int res = favoriteService.deleteFavorite(video_id, userId);
            if (res <= 0) return ResponseDto.failure("取消点赞失败，请重试！");
            else {
                redisTemplate.delete(key);
                return ResponseDto.success("取消点赞成功！");
            }
        } else {
            return ResponseDto.failure("用户行为不正确！");
        }
    }

    @GetMapping("/list")
    public ResponseDto<List<Video>> list (String token, String userId) {
        List<Video> videoList = null;

        if (!TokenUtils.verify(token)) return ResponseDto.failure("用户登录错误");
        if (userId == null) return ResponseDto.failure("查看用户不正确！");
        int user_id = Integer.parseInt(userId);
        if (user_id <= 0) return ResponseDto.failure("查看用户号不正确！");
        int guestId = Integer.parseInt(TokenUtils.parseId(token));

        String key = "favorite_" + userId + "_" + guestId;
        videoList = (List<Video>) redisTemplate.opsForValue().get(key);
        if (videoList != null) {
            return ResponseDto.success(videoList);
        }

        videoList = favoriteService.getVideosByUid(user_id, guestId).stream().map(item -> {
            item.setCoverUrl(imagePath + item.getCoverUrl());
            item.setPlayUrl(videoPath + item.getPlayUrl());
            return item;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, videoList, 60, TimeUnit.MINUTES);
        return ResponseDto.success(videoList);
    }
}

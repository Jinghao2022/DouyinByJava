package com.bytedance.douyinbyjava.controller;

import com.bytedance.douyinbyjava.entity.Video;
import com.bytedance.douyinbyjava.response.ResponseDto;
import com.bytedance.douyinbyjava.service.VideoService;
import com.bytedance.douyinbyjava.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("/douyin/publish")
public class VideoController {
    @Resource
    VideoService videoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${douyin.imagePath}")
    private String imagePath;

    @Value("${douyin.videoPath}")
    private String videoPath;

    @GetMapping("/list")
    public ResponseDto<List<Video>> list (String token, String userId) {
        List<Video> videoList = null;

        if (userId == null) return ResponseDto.failure("查找用户不存在！");
        if (!TokenUtils.verify(token)) return ResponseDto.failure("登录错误");

        int guest_id = Integer.parseInt(TokenUtils.parseId(token));
        int user_id = Integer.parseInt(userId);

        String key = "video_" + userId + "_" + guest_id;
        videoList = (List<Video>) redisTemplate.opsForValue().get(key);
        if (videoList != null) {
            return ResponseDto.success(videoList);
        }

        List<Video> videosByUid = videoService.getVideosByUid(user_id, guest_id);
        videoList = videosByUid.stream().map(item -> {
            item.setCoverUrl(imagePath + item.getCoverUrl());
            item.setPlayUrl(videoPath + item.getPlayUrl());
            return item;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, videoList, 60, TimeUnit.MINUTES);
        return ResponseDto.success(videoList);
    }

    @PostMapping("/action")
    public ResponseDto<String> action (String videoUrl, String imageUrl, String token, String title) {
        if (!TokenUtils.verify(token)) return ResponseDto.failure("上传视频用户出现错误！");

        int userId = Integer.parseInt(TokenUtils.parseId(token));
        int res = videoService.insertVideo(userId, videoUrl, imageUrl, title);
        if (res <= 0) return ResponseDto.failure("视频信息上传失败，请重试！");

        String key = "video_" + userId + "_" + userId;
        redisTemplate.delete(key);
        return ResponseDto.failure("视频信息上传成功！");
    }
}

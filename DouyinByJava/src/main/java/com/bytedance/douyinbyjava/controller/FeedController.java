package com.bytedance.douyinbyjava.controller;

import com.bytedance.douyinbyjava.entity.Video;
import com.bytedance.douyinbyjava.response.ResponseDto;
import com.bytedance.douyinbyjava.service.FeedService;
import com.bytedance.douyinbyjava.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/douyin")
public class FeedController {
    @Resource
    FeedService feedService;

    @Value("${douyin.imagePath}")
    private String imagePath;

    @Value("${douyin.videoPath}")
    private String videoPath;

    @GetMapping("/feed")
    public ResponseDto<List<Video>> feed(String token, String latest_time) {
        boolean flag = TokenUtils.verify(token);
        if (!flag && latest_time == null) {
            List<Video> videoList = feedService.getAllVideos().stream().map(item -> {
                item.setCoverUrl(imagePath + item.getCoverUrl());
                item.setPlayUrl(videoPath + item.getPlayUrl());
                return item;
            }).collect(Collectors.toList());
            int size = videoList.size();
            if (size == 0) return ResponseDto.success(videoList).add("next_time", LocalDateTime.now());
            return ResponseDto.success(videoList).add("next_time", videoList.get(size - 1).getUpdateTime());
        }


        if (!flag) {
            Date date = new Date(Long.parseLong(latest_time));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = dateFormat.format(date);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<Video> videoList = feedService.getAllVideosWithTime(LocalDateTime.parse(format, df))
                    .stream().map(item -> {
                item.setCoverUrl(imagePath + item.getCoverUrl());
                item.setPlayUrl(videoPath + item.getPlayUrl());
                return item;
            }).collect(Collectors.toList());
            int size = videoList.size();
            if (size == 0) return ResponseDto.success(videoList).add("next_time", LocalDateTime.now());
            return ResponseDto.success(videoList).add("next_time", videoList.get(size - 1).getUpdateTime());
        }

        if (latest_time == null) {
            int user_id = Integer.parseInt(TokenUtils.parseId(token));
            List<Video> videoList = feedService.getAllVideosWithTimeAndUser(LocalDateTime.now(), user_id)
                    .stream().map(item -> {
                item.setCoverUrl(imagePath + item.getCoverUrl());
                item.setPlayUrl(videoPath + item.getPlayUrl());
                return item;
            }).collect(Collectors.toList());
            int size = videoList.size();
            if (size == 0) return ResponseDto.success(videoList).add("next_time", LocalDateTime.now());
            return ResponseDto.success(videoList).add("next_time", videoList.get(size - 1).getUpdateTime());
        }

        Date date = new Date(Long.parseLong(latest_time));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = dateFormat.format(date);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int user_id = Integer.parseInt(TokenUtils.parseId(token));
        List<Video> videoList = feedService.getAllVideosWithTimeAndUser(LocalDateTime.parse(format, df), user_id)
                .stream().map(item -> {
            item.setCoverUrl(imagePath + item.getCoverUrl());
            item.setPlayUrl(videoPath + item.getPlayUrl());
            return item;
        }).collect(Collectors.toList());
        int size = videoList.size();
        if (size == 0) return ResponseDto.success(videoList).add("next_time", LocalDateTime.now());
        return ResponseDto.success(videoList).add("next_time", videoList.get(size - 1).getUpdateTime());
        //return ResponseDto.failure("失败！").add("next_time", LocalDateTime.now());
    }
}

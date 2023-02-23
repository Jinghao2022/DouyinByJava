package com.bytedance.douyinbyjava.service.impl;

import com.bytedance.douyinbyjava.entity.Video;
import com.bytedance.douyinbyjava.mapper.VideoMapper;
import com.bytedance.douyinbyjava.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    VideoMapper videoMapper;

    @Override
    public List<Video> getVideosByUid(int userId, int guestId) {
        return videoMapper.getVideoByUid(userId, guestId);
    }

    @Override
    public Video getVideoInfo(int videoId, int userId) {
        return videoMapper.getVideoInfo(videoId, userId);
    }

    @Override
    public int insertVideo(int userId, String playUrl, String coverUrl, String title) {
        return videoMapper.insertVideo(userId, playUrl, coverUrl, title);
    }
}

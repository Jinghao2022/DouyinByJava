package com.bytedance.douyinbyjava.service.impl;

import com.bytedance.douyinbyjava.entity.Video;
import com.bytedance.douyinbyjava.mapper.FeedMapper;
import com.bytedance.douyinbyjava.service.FeedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {
    @Resource
    FeedMapper feedMapper;

    @Override
    public List<Video> getAllVideos() {
        return feedMapper.getAllVideos();
    }

    @Override
    public List<Video> getAllVideosWithTime(LocalDateTime latestTime) {
        return feedMapper.getAllVideosWithTime(latestTime);
    }

    @Override
    public List<Video> getAllVideosWithTimeAndUser(LocalDateTime latestTime, int userId) {
        return feedMapper.getAllVideosWithTimeAndUser(latestTime, userId);
    }
}

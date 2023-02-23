package com.bytedance.douyinbyjava.service;

import com.bytedance.douyinbyjava.entity.Video;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedService {
    List<Video> getAllVideos();

    List<Video> getAllVideosWithTime(LocalDateTime latestTime);

    List<Video> getAllVideosWithTimeAndUser(LocalDateTime latestTime, int userId);
}

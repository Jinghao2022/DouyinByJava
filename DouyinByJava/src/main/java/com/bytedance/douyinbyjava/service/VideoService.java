package com.bytedance.douyinbyjava.service;

import com.bytedance.douyinbyjava.entity.Video;

import java.util.List;

public interface VideoService {
    List<Video> getVideosByUid (int userId, int guestId);

    Video getVideoInfo (int videoId, int userId);

    int insertVideo (int userId, String playUrl, String coverUrl, String title);
}

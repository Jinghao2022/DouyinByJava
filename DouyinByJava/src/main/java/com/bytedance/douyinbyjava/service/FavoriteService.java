package com.bytedance.douyinbyjava.service;

import com.bytedance.douyinbyjava.entity.Video;

import java.util.List;

public interface FavoriteService {
    int deleteFavorite (int videoId, int userId);

    int insertFavorite (int videoId, int userId);

    List<Video> getVideosByUid (int userId, int guestId);

    int getUidByVid (int videoId);
}

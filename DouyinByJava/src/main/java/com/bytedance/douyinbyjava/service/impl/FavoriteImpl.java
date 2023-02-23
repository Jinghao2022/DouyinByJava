package com.bytedance.douyinbyjava.service.impl;

import com.bytedance.douyinbyjava.entity.Video;
import com.bytedance.douyinbyjava.mapper.FavoriteMapper;
import com.bytedance.douyinbyjava.service.FavoriteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FavoriteImpl implements FavoriteService {
    @Resource
    FavoriteMapper favoriteMapper;

    @Override
    public int deleteFavorite(int videoId, int userId) {
        return favoriteMapper.deleteFavorite(videoId, userId);
    }

    @Override
    public int insertFavorite(int videoId, int userId) {
        return favoriteMapper.insertFavorite(videoId, userId);
    }

    @Override
    public List<Video> getVideosByUid(int userId, int guestId) {
        return favoriteMapper.getVideosByUid(userId, guestId);
    }

    @Override
    public int getUidByVid(int videoId) {
        return favoriteMapper.getUidByVid(videoId);
    }
}

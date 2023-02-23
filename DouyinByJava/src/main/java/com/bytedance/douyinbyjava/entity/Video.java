package com.bytedance.douyinbyjava.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Video implements Serializable {
    /**
     * 视频作者信息
     */
    private UserInfo author;

    /**
     * 视频的评论总数
     */
    private long commentCount;

    /**
     * 视频封面地址
     */
    private String coverUrl;

    /**
     * 视频的点赞总数
     */
    private long favoriteCount;

    /**
     * 视频唯一标识
     */
    private long id;

    /**
     * 视频播放地址
     */
    private String playUrl;

    /**
     * true-已点赞，false-未点赞
     */
    private boolean isFavorite;

    /**
     * 视频标题
     */
    private String title;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

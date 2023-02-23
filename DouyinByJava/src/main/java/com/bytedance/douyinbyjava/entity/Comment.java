package com.bytedance.douyinbyjava.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment implements Serializable {
    /**
     * 评论内容
     */
    private String commentText;
    /**
     * 评论id
     */
    private long id;
    /**
     * 评论用户信息
     */
    private UserInfo user;

    private int userId;

    private int videoId;

    private Video video;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

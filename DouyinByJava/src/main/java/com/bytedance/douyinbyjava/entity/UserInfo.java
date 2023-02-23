package com.bytedance.douyinbyjava.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserInfo implements Serializable {
    private String username;

    private String password;

    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户个人页顶部大图
     */
    private String backgroundImage;
    /**
     * 喜欢数
     */
    private int favoriteCount;
    /**
     * 关注总数
     */
    private int followCount;
    /**
     * 粉丝总数
     */
    private int followerCount;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * true-已关注，false-未关注
     */
    private boolean isFollow;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 个人简介
     */
    private String signature;
    /**
     * 获赞数量
     */
    private int totalFavorited;
    /**
     * 作品数
     */
    private int workCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

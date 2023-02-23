package com.bytedance.douyinbyjava.mapper;

import com.bytedance.douyinbyjava.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoMapper {
    @Results({
            @Result(id = true, column = "video_id", property = "id"),
            @Result(column = "{user_id = author_id, guest_id = guest_id}", property = "author", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.UserMapper.getUserInfo")
            ),
            @Result(column = "video_id", property = "commentCount", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.FeedMapper.getCommentCount")
            ),
            @Result(column = "video_id", property = "favoriteCount", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.FeedMapper.getFavoriteCount")
            ),
            @Result(column = "isfavorite", property = "isFavorite")
    })
    @Select("select video.*, #{guest_id} as guest_id, " +
            "case when (isnull(favorite_id) > 0) then false else true end as isfavorite " +
            "from video left join (select * from favorite where user_id = #{guest_id}) t " +
            "on video.video_id = t.video_id " +
            "where video.author_id = #{user_id} order by update_time desc")
    List<Video> getVideoByUid (@Param("user_id") int userId, @Param("guest_id") int guestId);

    @Results({
            @Result(id = true, column = "video_id", property = "id"),
            @Result(column = "{user_id = author_id, guest_id = guest_id}", property = "author", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.UserMapper.getUserInfo")
            ),
            @Result(column = "video_id", property = "commentCount", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.FeedMapper.getCommentCount")
            ),
            @Result(column = "video_id", property = "favoriteCount", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.FeedMapper.getFavoriteCount")
            ),
            @Result(column = "isfavorite", property = "isFavorite")
    })
    @Select("select *, #{userId} as guest_id, " +
            "exists (select favorite_id from favorite where video_id = #{videoId} and user_id = #{userId}) as isfavorite " +
            "from video where video_id = #{videoId}")
    Video getVideoInfo (@Param("videoId") int videoId, @Param("userId") int guestId);

    @Insert("insert into video(author_id, play_url, cover_url, title, create_time, update_time)" +
            "VALUES (#{userId}, #{playUrl}, #{coverUrl}, #{title}, NOW(), NOW())")
    int insertVideo (@Param("userId") int userId, @Param("playUrl") String playUrl,
                     @Param("coverUrl") String coverUrl, @Param("title") String title);
}

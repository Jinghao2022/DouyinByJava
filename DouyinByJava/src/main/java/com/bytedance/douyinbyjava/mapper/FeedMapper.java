package com.bytedance.douyinbyjava.mapper;

import com.bytedance.douyinbyjava.entity.UserInfo;
import com.bytedance.douyinbyjava.entity.Video;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FeedMapper {
    @Results({
            @Result(id = true, column = "video_id", property = "id"),
            @Result(column = "author_id", property = "author", one =
                @One(select = "getUserById")
            ),
            @Result(column = "video_id", property = "commentCount", one =
                @One(select = "getCommentCount")
            ),
            @Result(column = "video_id", property = "favoriteCount", one =
                @One(select = "getFavoriteCount")
            )
    })
    @Select("select * from video order by update_time desc limit 30")
    List<Video> getAllVideos();

    @Results({
            @Result(id = true, column = "video_id", property = "id"),
            @Result(column = "author_id", property = "author", one =
            @One(select = "getUserById")
            ),
            @Result(column = "video_id", property = "commentCount", one =
            @One(select = "getCommentCount")
            ),
            @Result(column = "video_id", property = "favoriteCount", one =
            @One(select = "getFavoriteCount")
            )
    })
    @Select("select * from video where update_time <= #{latestTime} order by update_time desc limit 30")
    List<Video> getAllVideosWithTime(LocalDateTime latestTime);

    @Results({
            @Result(id = true, column = "video_id", property = "id"),
            @Result(column = "{user_id = author_id, guest_id = guest_id}", property = "author", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.UserMapper.getUserInfo")
            ),
            @Result(column = "video_id", property = "commentCount", one =
                @One(select = "getCommentCount")
            ),
            @Result(column = "video_id", property = "favoriteCount", one =
                @One(select = "getFavoriteCount")
            ),
            @Result(column = "isfavorite", property = "isFavorite")
    })
    @Select("select video.*, #{user_id} as guest_id, " +
            "case when (isnull(favorite_id) > 0) then false else true end as isfavorite " +
            "from video left join (select * from favorite where user_id = #{user_id}) t " +
            "on video.video_id = t.video_id " +
            "where video.update_time <= #{latest_time} order by video.update_time desc limit 30")
    List<Video> getAllVideosWithTimeAndUser(@Param("latest_time") LocalDateTime latestTime, @Param("user_id") int userId);

    @Select("select * from users where user_id = #{userId}")
    UserInfo getUserById(int userId);

    @Select("select COUNT(*) from comments where video_id = #{videoId}")
    int getCommentCount(int videoId);

    @Select("select COUNT(*) from favorite where video_id = #{videoId}")
    int getFavoriteCount(int videoId);

}

package com.bytedance.douyinbyjava.mapper;

import com.bytedance.douyinbyjava.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    @Insert("insert into favorite (user_id, video_id, create_time, update_time) values (#{userId}, #{videoId}, NOW(), NOW())")
    int insertFavorite (@Param("videoId") int videoId, @Param("userId") int userId);

    @Delete("delete from favorite where user_id = #{userId} and video_id = #{videoId}")
    int deleteFavorite (@Param("videoId") int videoId, @Param("userId") int userId);

    @Results({
            @Result(id = true, column = "favorite_id", property = "id"),
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
            "case when (isnull(t2.favorite_id) > 0) then false else true end as isfavorite " +
            "from video inner join (select * from favorite where user_id = #{user_id}) t1 on video.video_id = t1.video_id " +
            "left join (select * from favorite where user_id = #{guest_id}) t2 " +
            "on video.video_id = t2.video_id order by update_time desc")
    List<Video> getVideosByUid (@Param("user_id") int userId, @Param("guest_id") int guestId);

    @Select("select author_id from video where video_id = #{videoId}")
    int getUidByVid (int videoId);
}

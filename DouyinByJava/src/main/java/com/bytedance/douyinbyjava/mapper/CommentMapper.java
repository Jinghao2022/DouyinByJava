package com.bytedance.douyinbyjava.mapper;

import com.bytedance.douyinbyjava.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Results({
            @Result(id = true, column = "comment_id", property = "id"),
            @Result(column = "{user_id = user_id, guest_id = guest_id}", property = "user", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.UserMapper.getUserInfo")
            ),
            @Result(column = "{videoId = video_id, userId = guest_id}", property = "video", one =
                @One(select = "com.bytedance.douyinbyjava.mapper.VideoMapper.getVideoInfo")
            )
    })
    @Select("select *, #{guestId} as guest_id from comments where video_id = #{videoId}")
    List<Comment> getCommentsByVid (@Param("videoId") int videoId, @Param("guestId") int guestId);

    @Insert("insert into comments (user_id, video_id, comment_text, create_time, update_time)" +
            "values (#{userId}, #{videoId}, #{commentText}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "comment_id")
    int insertComment (Comment comment);

    @Delete("delete from comments where comment_id = #{commentId}")
    int deleteComment (int commentId);

    @Results({
            @Result(id = true, column = "comment_id", property = "id"),
            @Result(column = "{user_id = user_id, guest_id = user_id}", property = "user", one =
            @One(select = "com.bytedance.douyinbyjava.mapper.UserMapper.getUserInfo")
            ),
            @Result(column = "{videoId = video_id, userId = user_id}", property = "video", one =
            @One(select = "com.bytedance.douyinbyjava.mapper.VideoMapper.getVideoInfo")
            )
    })
    @Select("select * from comments where comment_id = #{id}")
    Comment getCommentInfo (int id);
}

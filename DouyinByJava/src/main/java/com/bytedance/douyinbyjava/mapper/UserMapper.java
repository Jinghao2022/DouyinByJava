package com.bytedance.douyinbyjava.mapper;

import com.bytedance.douyinbyjava.entity.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into users (username, password, create_time, update_time) values (#{username}, #{password}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    int insertUser(UserInfo userInfo);

    @Select("select * from users where username = #{username}")
    UserInfo getUser(String username);

    @Select("select * from users where user_id = #{user_id}")
    UserInfo getUserById(int userId);

    @Results(value = {
            @Result(id = true, column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "followCount", one =
                @One(select = "getFollowCount")
            ),
            @Result(column = "user_id", property = "followerCount", one =
                @One(select = "getFollowerCount")
            ),
            @Result(column = "user_id", property = "favoriteCount", one =
                @One(select = "getFavoriteCount")
            ),
            @Result(column = "user_id", property = "workCount", one =
                @One(select = "getWorkCount")
            ),
            @Result(column = "user_id", property = "totalFavorited", one =
                @One(select = "getTotalFavorite")
            )
    })
    @Select("select *," +
            "exists (select follow_id from follow where host_id = #{user_id} and guest_id = #{guest_id}) as isfollow " +
            "from users where user_id = #{user_id}")
    UserInfo getUserInfo(@Param("user_id") Integer user_id, @Param("guest_id") Integer guestId);

    @Select("select COUNT(*) from follow where guest_id = #{user_id}")
    int getFollowCount(Integer user_id);

    @Select("select COUNT(*) from follow where host_id = #{user_id}")
    int getFollowerCount(Integer user_id);

    @Select("select COUNT(*) from favorite where video_id in (select video_id from video where author_id = #{user_id})")
    int getTotalFavorite(Integer user_id);

    @Select("select COUNT(*) from video where author_id = #{user_id}")
    int getWorkCount(Integer user_id);

    @Select("select COUNT(*) from favorite where user_id = #{user_id}")
    int getFavoriteCount(Integer user_id);
}

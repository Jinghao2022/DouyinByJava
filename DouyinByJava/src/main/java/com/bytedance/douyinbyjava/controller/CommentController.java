package com.bytedance.douyinbyjava.controller;

import com.bytedance.douyinbyjava.entity.Comment;
import com.bytedance.douyinbyjava.entity.Video;
import com.bytedance.douyinbyjava.mapper.UserMapper;
import com.bytedance.douyinbyjava.mapper.VideoMapper;
import com.bytedance.douyinbyjava.response.ResponseDto;
import com.bytedance.douyinbyjava.service.CommentService;
import com.bytedance.douyinbyjava.service.UserService;
import com.bytedance.douyinbyjava.service.VideoService;
import com.bytedance.douyinbyjava.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/douyin/comment")
public class CommentController {
    @Resource
    CommentService commentService;

    @Resource
    UserService userService;

    @Resource
    VideoService videoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${douyin.imagePath}")
    private String imagePath;

    @Value("${douyin.videoPath}")
    private String videoPath;

    @GetMapping("/list")
    public ResponseDto<List<Comment>> list (String token, String videoId) {
        List<Comment> commentList = null;

        if (!TokenUtils.verify(token)) return ResponseDto.failure("用户登录错误");

        if (videoId == null) return ResponseDto.failure("视频不正确！");

        int video_id = Integer.parseInt(videoId);
        if (video_id <= 0) return ResponseDto.failure("视频号不正确！");

        int userId = Integer.parseInt(TokenUtils.parseId(token));

        String key = "comment_" + videoId + "_" + userId;
        commentList = (List<Comment>) redisTemplate.opsForValue().get(key);
        if (commentList != null) {
            return ResponseDto.success(commentList);
        }

        commentList = commentService.getCommentsByVid(video_id, userId);
        if (commentList.size() != 0) {
            Video video = commentList.get(0).getVideo();
            String playUrl = video.getPlayUrl();
            String coverUrl = video.getCoverUrl();
            video.setPlayUrl(videoPath + playUrl);
            video.setCoverUrl(imagePath + coverUrl);
        }

        redisTemplate.opsForValue().set(key, commentList);
        return ResponseDto.success(commentList);
    }

    @PostMapping("/action")
    @Transactional
    public ResponseDto<Comment> action (String token, String videoId, String actionType, String commentText, int commentId) {
        if (!TokenUtils.verify(token)) return ResponseDto.failure("用户登录错误");

        if (videoId == null) return ResponseDto.failure("视频不正确！");

        int video_id = Integer.parseInt(videoId);
        if (video_id <= 0) return ResponseDto.failure("视频号不正确！");

        if (actionType == null) return ResponseDto.failure("用户行为不正确！");

        int action_type = Integer.parseInt(actionType);
        int userId = Integer.parseInt(TokenUtils.parseId(token));

        String key = "comment_" + videoId + "_" + userId;

        if (action_type == 1) {
            Comment comment = new Comment();
            comment.setCommentText(commentText);
            comment.setUserId(userId);
            comment.setVideoId(video_id);
            Video video = videoService.getVideoInfo(video_id, userId);
            video.setCoverUrl(imagePath + video.getCoverUrl());
            video.setPlayUrl(videoPath + video.getPlayUrl());
            comment.setVideo(video);
            comment.setUser(userService.getUserInfo(userId, userId));
            int res = commentService.insertComment(comment);
            if (res == 0) return ResponseDto.failure("评论失败，请重新评论！");

            redisTemplate.delete(key);
            return ResponseDto.success(comment);
        } else if (action_type == 2) {
            int res = commentService.deleteComment(commentId);
            if (res == 0) return ResponseDto.failure("删除评论失败，请重新删除！");

            redisTemplate.delete(key);
            return ResponseDto.success(null);
        } else {
            return ResponseDto.failure("评论行为错误！");
        }
    }
}

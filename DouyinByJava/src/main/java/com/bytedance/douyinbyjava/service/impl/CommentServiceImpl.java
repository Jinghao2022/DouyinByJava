package com.bytedance.douyinbyjava.service.impl;

import com.bytedance.douyinbyjava.entity.Comment;
import com.bytedance.douyinbyjava.mapper.CommentMapper;
import com.bytedance.douyinbyjava.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    CommentMapper commentMapper;


    @Override
    public List<Comment> getCommentsByVid(int videoId, int guestId) {
        return commentMapper.getCommentsByVid(videoId, guestId);
    }

    @Override
    public int insertComment(Comment comment) {
        return commentMapper.insertComment(comment);
    }

    @Override
    public int deleteComment(int commentId) {
        return commentMapper.deleteComment(commentId);
    }

    @Override
    public Comment getCommentInfo(int id) {
        return commentMapper.getCommentInfo(id);
    }
}

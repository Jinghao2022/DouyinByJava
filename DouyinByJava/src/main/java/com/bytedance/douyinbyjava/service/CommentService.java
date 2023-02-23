package com.bytedance.douyinbyjava.service;

import com.bytedance.douyinbyjava.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByVid(int videoId, int guestId);

    int insertComment (Comment comment);

    int deleteComment (int commentId);

    Comment getCommentInfo(int id);
}

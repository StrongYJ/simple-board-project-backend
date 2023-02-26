package com.myproject.simpleboard.domain.post.dto.comment;

import java.time.LocalDateTime;

import com.myproject.simpleboard.domain.post.entity.Comment;

public record CommentDto(Long id, String content, String author, LocalDateTime created, LocalDateTime updated) {
    
    public CommentDto(Comment entity) {
        this(entity.getId(), entity.getContent(), entity.getAuthor(), entity.getCreatedDate(), entity.getModifiedDate());
    }
}

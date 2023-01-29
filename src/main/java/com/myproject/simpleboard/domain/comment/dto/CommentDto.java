package com.myproject.simpleboard.domain.comment.dto;

import java.time.LocalDateTime;

import com.myproject.simpleboard.domain.comment.Comment;

public record CommentDto(Long id, String content, String writer, LocalDateTime created, LocalDateTime updated) {
    
    public CommentDto(Comment entity) {
        this(entity.getId(), entity.getContent(), entity.getWriter(), entity.getCreatedDT(), entity.getUpdatedDT());
    }
}

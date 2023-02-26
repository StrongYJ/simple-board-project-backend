package com.myproject.simpleboard.domain.post.dto.post;

import com.myproject.simpleboard.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostDto(Long id, String board, String type, String title, String body,
                      String author, LocalDateTime createdDate, LocalDateTime modifiedDate) {
    public PostDto(Post entity) {
        this(entity.getId(), entity.getBoard(), entity.getType(), entity.getTitle(), entity.getBody(),
                entity.getAuthor(), entity.getCreatedDate(), entity.getModifiedDate());
    }
    
}

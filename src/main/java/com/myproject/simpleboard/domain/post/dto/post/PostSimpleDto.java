package com.myproject.simpleboard.domain.post.dto.post;

import com.myproject.simpleboard.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostSimpleDto(Long id, String type, String title, String author, LocalDateTime created, LocalDateTime updated) {

    public PostSimpleDto(Post post) {
        this(post.getId(), post.getType(), post.getTitle(), post.getAuthor(),
                post.getCreatedDate(), post.getModifiedDate());
    }

}

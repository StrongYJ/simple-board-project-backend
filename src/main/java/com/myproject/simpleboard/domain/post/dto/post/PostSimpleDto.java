package com.myproject.simpleboard.domain.post.dto.post;

import java.time.LocalDateTime;

import com.myproject.simpleboard.domain.post.entity.Post;

public record PostSimpleDto(Long id, String title, String wrtier, LocalDateTime created, LocalDateTime updated) {

    public PostSimpleDto(Post post) {
        this(post.getId(), post.getTitle(), post.getWriter(), post.getCreatedDate(), post.getModifiedDate());
    }

}

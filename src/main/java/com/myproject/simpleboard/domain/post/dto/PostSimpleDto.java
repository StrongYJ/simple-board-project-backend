package com.myproject.simpleboard.domain.post.dto;

import java.time.LocalDateTime;

import com.myproject.simpleboard.domain.post.domain.Post;

public record PostSimpleDto(Long id, String title, String wrtier, LocalDateTime created, LocalDateTime updated) {

    public PostSimpleDto(Post post) {
        this(post.getId(), post.getTitle(), post.getWriter(), post.getCreatedDT(), post.getUpdatedDT());
    }

}

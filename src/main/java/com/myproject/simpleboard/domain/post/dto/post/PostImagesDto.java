package com.myproject.simpleboard.domain.post.dto.post;

import com.myproject.simpleboard.domain.post.entity.PostImage;

public record PostImagesDto(Long id, String uri) {
    public PostImagesDto {
        uri = "/images/" + uri;
    }

    public PostImagesDto(PostImage entity) {
        this(entity.getId(), entity.getSavedName());
    }
}

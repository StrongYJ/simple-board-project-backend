package com.myproject.simpleboard.domain.post.dto;

import com.myproject.simpleboard.domain.post.domain.PostImage;

public record PostImagesDto(Long id, String uri) {
    public PostImagesDto {
        uri = "/images/" + uri;
    }

    public PostImagesDto(PostImage entity) {
        this(entity.getId(), entity.getSavedName());
    }
}

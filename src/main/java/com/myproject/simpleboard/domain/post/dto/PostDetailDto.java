package com.myproject.simpleboard.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.myproject.simpleboard.domain.comment.dto.CommentDto;
import com.myproject.simpleboard.domain.post.domain.Post;

public record PostDetailDto(
    Long id, String title, String wrtier, LocalDateTime created, LocalDateTime updated,
    String body, List<PostImagesDto> images, List<CommentDto> comments
) {
    
    public PostDetailDto(Post entity) {
        this(
            entity.getId(), entity.getTitle(), entity.getWriter(), entity.getCreatedDT(), entity.getUpdatedDT(), 
            entity.getBody(),
            entity.getImages().stream().map(PostImagesDto::new).toList(),
            entity.getComments().stream().map(CommentDto::new).toList()
        );
    }

}

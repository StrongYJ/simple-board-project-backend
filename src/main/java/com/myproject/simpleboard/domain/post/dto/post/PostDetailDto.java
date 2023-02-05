package com.myproject.simpleboard.domain.post.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.myproject.simpleboard.domain.post.entity.Comment;
import com.myproject.simpleboard.domain.post.entity.Post;
import com.myproject.simpleboard.domain.post.dto.comment.CommentDto;

public record PostDetailDto(
    Long id, String title, String wrtier, LocalDateTime created, LocalDateTime updated,
    String body, List<PostImagesDto> images, List<CommentDto> comments
) {
    
    public PostDetailDto(Post post, List<Comment> comments) {
        this(
            post.getId(), post.getTitle(), post.getWriter(), post.getCreatedDate(), post.getModifiedDate(),
            post.getBody(),
            post.getImages().stream().map(PostImagesDto::new).toList(),
            comments.stream().map(CommentDto::new).toList()
        );
    }

}

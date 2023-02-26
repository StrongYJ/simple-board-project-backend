package com.myproject.simpleboard.domain.post.dto.post;

import com.myproject.simpleboard.domain.post.dto.comment.CommentDto;
import com.myproject.simpleboard.domain.post.entity.Comment;
import com.myproject.simpleboard.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailDto(
        Long id, String title, String author, LocalDateTime created, LocalDateTime updated,
        String body, List<PostImagesDto> images, List<CommentDto> comments
) {
    
    public PostDetailDto(Post post, List<Comment> comments) {
        this(
            post.getId(), post.getTitle(), post.getAuthor(), post.getCreatedDate(), post.getModifiedDate(),
            post.getBody(),
            post.getImages().stream().map(PostImagesDto::new).toList(),
            comments.stream().map(CommentDto::new).toList()
        );
    }

}

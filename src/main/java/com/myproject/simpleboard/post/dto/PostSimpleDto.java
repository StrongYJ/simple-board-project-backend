package com.myproject.simpleboard.post.dto;

import com.myproject.simpleboard.post.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSimpleDto {
    private Long id;
    private String title;
    private String writer;

    public PostSimpleDto(Post post) {
        id = post.getId();
        title = post.getTitle();
        writer = post.getWriter().getWriterName();
    }
}

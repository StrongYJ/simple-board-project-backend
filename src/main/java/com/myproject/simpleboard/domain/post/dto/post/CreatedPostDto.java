package com.myproject.simpleboard.domain.post.dto.post;

import java.time.LocalDateTime;

public record CreatedPostDto(Long id, String title, String body, String writer, LocalDateTime createdDT) {
    
}

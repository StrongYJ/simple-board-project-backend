package com.myproject.simpleboard.domain.post.dto.post;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public record PostCreateDto(
    @NotBlank String title,
    @NotBlank String body
) {
    
}

package com.myproject.simpleboard.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.simpleboard.post.dto.PostSimpleDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    
    private final PostRepository postRepo;

    public Page<PostSimpleDto> findAll(Pageable pageable) {
        return postRepo.findAll(pageable).map(PostSimpleDto::new);
    }
    
}

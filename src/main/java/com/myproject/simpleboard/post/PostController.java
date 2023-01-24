package com.myproject.simpleboard.post;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.simpleboard.global.common.responsebody.Body;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    
    @GetMapping("/posts")
    public ResponseEntity<Body> getPosts(Pageable pageable) {
        return ResponseEntity.ok().body(new Body(postService.findAll(pageable)));
    }
}

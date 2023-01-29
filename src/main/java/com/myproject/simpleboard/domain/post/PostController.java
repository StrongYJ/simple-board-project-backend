package com.myproject.simpleboard.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.simpleboard.domain.post.dto.CreatedPostDto;
import com.myproject.simpleboard.domain.post.dto.PostCreateDto;
import com.myproject.simpleboard.domain.post.dto.PostDetailDto;
import com.myproject.simpleboard.domain.post.dto.PostSimpleDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    
    private final PostService postService;
    
    @GetMapping("/posts")
    public ResponseEntity<Page<PostSimpleDto>> getPosts(Pageable pageable) {
        return ResponseEntity.ok().body(postService.findAll(pageable));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailDto> getPostDetail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(postService.findPostDetail(id), HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<CreatedPostDto> postCreatePost(
        @RequestPart(name = "text") @Validated PostCreateDto postCreateDto, 
        @RequestPart(required = false) MultipartFile... images
    ) {
        CreatedPostDto createdPost = postService.create(1L, postCreateDto, images);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }
}

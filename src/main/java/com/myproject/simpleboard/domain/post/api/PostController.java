package com.myproject.simpleboard.domain.post.api;

import com.myproject.simpleboard.domain.post.service.PostService;
import com.myproject.simpleboard.global.security.Auth;
import com.myproject.simpleboard.global.security.AuthMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.simpleboard.domain.post.dto.post.PostDto;
import com.myproject.simpleboard.domain.post.dto.post.PostCreateDto;
import com.myproject.simpleboard.domain.post.dto.post.PostDetailDto;
import com.myproject.simpleboard.domain.post.dto.post.PostSimpleDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
public class PostController {
    
    private final PostService postService;
    
    @GetMapping("/{board}")
    public ResponseEntity<Page<PostSimpleDto>> getPosts(@PathVariable("board") String board, Pageable pageable) {
        return ResponseEntity.ok().body(postService.findAllByBoard(board, pageable));
    }

    @GetMapping("/{board}/{id}")
    public ResponseEntity<PostDetailDto> getPostDetail(
            @PathVariable("board") String board,
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(postService.findPostDetail(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PostDto> postCreatePost(
            @Auth AuthMember authMember,
            @RequestPart(name = "text") @Validated PostCreateDto postCreateDto,
            @RequestPart(required = false) MultipartFile... images
            ) {
        PostDto createdPost = postService.create(authMember.id(), postCreateDto, images);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }
}

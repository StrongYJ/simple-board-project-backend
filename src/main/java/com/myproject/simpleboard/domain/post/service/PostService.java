package com.myproject.simpleboard.domain.post.service;

import java.util.List;

import com.myproject.simpleboard.domain.post.entity.Comment;
import com.myproject.simpleboard.domain.post.dao.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.post.dao.PostImageRepository;
import com.myproject.simpleboard.domain.post.dao.PostRepository;
import com.myproject.simpleboard.domain.post.entity.Post;
import com.myproject.simpleboard.domain.post.entity.PostImage;
import com.myproject.simpleboard.domain.post.dto.post.CreatedPostDto;
import com.myproject.simpleboard.domain.post.dto.post.PostCreateDto;
import com.myproject.simpleboard.domain.post.dto.post.PostDetailDto;
import com.myproject.simpleboard.domain.post.dto.post.PostSimpleDto;
import com.myproject.simpleboard.global.util.file.FileUtility;
import com.myproject.simpleboard.global.util.file.UploadFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepo;
    private final PostImageRepository postImageRepo;
    private final CommentRepository commentRepo;
    private final MemberRepository memberRepo;
    private final FileUtility fileUtility;

    public Page<PostSimpleDto> findAll(Pageable pageable) {
        return postRepo.findAll(pageable).map(PostSimpleDto::new);
    }

    public PostDetailDto findPostDetail(Long id) {
        Post post = postRepo.findPostByIdWithImages(id).orElseThrow();
        List<Comment> comments = commentRepo.findAllByPost(post);

        return new PostDetailDto(post, comments);
    }

    @Transactional
    public CreatedPostDto create(Long memberId, PostCreateDto text, MultipartFile... images) {
        Member member = memberRepo.findById(memberId).orElseThrow();
        Post newPost = new Post(text.title(), text.body(), member);
        postRepo.save(newPost);
        if (!ObjectUtils.isEmpty(images)) {
            List<UploadFile> saveFile = fileUtility.saveFile(images);
            List<PostImage> newImages = saveFile.stream()
                    .map(f -> new PostImage(f.originalFilename(), f.savedFilename(), newPost))
                    .toList();
            postImageRepo.saveAll(newImages);
        }
        return new CreatedPostDto(newPost.getId(), newPost.getTitle(), newPost.getBody(), newPost.getWriter(), newPost.getCreatedDate());
    }
}

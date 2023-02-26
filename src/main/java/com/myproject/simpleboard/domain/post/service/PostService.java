package com.myproject.simpleboard.domain.post.service;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.post.dto.post.*;
import com.myproject.simpleboard.domain.post.entity.Comment;
import com.myproject.simpleboard.domain.post.entity.Post;
import com.myproject.simpleboard.domain.post.entity.PostImage;
import com.myproject.simpleboard.domain.post.repository.CommentRepository;
import com.myproject.simpleboard.domain.post.repository.PostImageRepository;
import com.myproject.simpleboard.domain.post.repository.PostRepository;
import com.myproject.simpleboard.global.security.AuthMember;
import com.myproject.simpleboard.global.util.file.FileUtility;
import com.myproject.simpleboard.global.util.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepo;
    private final PostImageRepository postImageRepo;
    private final CommentRepository commentRepo;
    private final MemberRepository memberRepo;
    private final FileUtility fileUtility;

    public Page<PostSimpleDto> findAllByBoard(String board, Pageable pageable) {
        return postRepo.findByBoard(board, pageable).map(PostSimpleDto::new);
    }

    public PostDetailDto findPostDetail(Long id) {
        Post post = postRepo.findPostByIdWithImages(id).orElseThrow();
        List<Comment> comments = commentRepo.findAllByPost(post);

        return new PostDetailDto(post, comments);
    }

    @Transactional
    public PostDto create(Long memberId, PostCreateDto data, MultipartFile... images) {
        if(!postRepo.existsByBoard(data.board())) {
            throw new NoSuchElementException("존재하지 않는 게시판입니다.");
        }

        Member member = memberRepo.findById(memberId).orElseThrow();
        Post newPost = new Post(data.board(), data.type(), data.title(), data.body(), member);
        postRepo.save(newPost);
        if (!ObjectUtils.isEmpty(images)) {
            saveImages(newPost, images);
        }
        return new PostDto(newPost);
    }

    @Transactional
    public PostDto update(Long postId, PostUpdateDto data, Long memberId, MultipartFile... images) {
        Post post = postRepo.findById(postId).orElseThrow();
        Member member = memberRepo.findById(memberId).orElseThrow();

        if(post.getMember() == null) {
            throw new IllegalArgumentException("수정할 수 없는 게시물입니다.");
        } else {
            if(post.getMember().getId() != member.getId()) {
                throw new IllegalArgumentException("본인이 작성한 게시물만 수정할 수 있습니다.");
            }
        }
        
        deleteImages(post);
        if(!ObjectUtils.isEmpty(images))
            saveImages(post, images);

        post.updateTextInfo(data);
        return new PostDto(post);
    }

    @Transactional
    public void delete(long postId, AuthMember authMember) {
        Post post = postRepo.findById(postId).orElseThrow();

        if(authMember.role() == MemberRole.USER &&
                post.getMember() != null && post.getMember().getId() != authMember.id()) {
            throw new IllegalArgumentException("본인 게시물만 삭제할 수 있습니다.");
        }
        deleteImages(post);
        postRepo.delete(post);
    }

    private void deleteImages(Post post) {
        List<PostImage> images = postImageRepo.findByPost(post);
        images.forEach(img -> new File(fileUtility.getFullPath(img.getSavedName()).toString()).delete());
        postImageRepo.deleteByPost(post);
    }

    private void saveImages(Post post, MultipartFile[] images) {
        List<UploadFile> saveFile = fileUtility.save(images);
        List<PostImage> newImages = saveFile.stream()
                .map(f -> new PostImage(f.originalFilename(), f.savedFilename(), post))
                .toList();
        postImageRepo.saveAll(newImages);
    }
}

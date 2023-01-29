package com.myproject.simpleboard.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.domain.Member;
import com.myproject.simpleboard.domain.post.dao.PostImageRepository;
import com.myproject.simpleboard.domain.post.dao.PostRepository;
import com.myproject.simpleboard.domain.post.dao.PostRepositoryCustom;
import com.myproject.simpleboard.domain.post.domain.Post;
import com.myproject.simpleboard.domain.post.domain.PostImage;
import com.myproject.simpleboard.domain.post.dto.CreatedPostDto;
import com.myproject.simpleboard.domain.post.dto.PostCreateDto;
import com.myproject.simpleboard.domain.post.dto.PostDetailDto;
import com.myproject.simpleboard.domain.post.dto.PostSimpleDto;
import com.myproject.simpleboard.global.util.file.FileUtility;
import com.myproject.simpleboard.global.util.file.UploadFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepo;
    private final PostRepositoryCustom postRepoCustom;
    private final MemberRepository memberRepo;
    private final PostImageRepository postImageRepo;
    private final FileUtility fileUtility;

    public Page<PostSimpleDto> findAll(Pageable pageable) {
        return postRepo.findAll(pageable).map(PostSimpleDto::new);
    }

    public PostDetailDto findPostDetail(Long id) {
        return new PostDetailDto(postRepoCustom.findByIdWithComments(id).orElseThrow());
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
        return new CreatedPostDto(newPost.getId(), newPost.getTitle());
    }
}

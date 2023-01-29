package com.myproject.simpleboard.global.data;

import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import com.myproject.simpleboard.domain.comment.Comment;
import com.myproject.simpleboard.domain.comment.CommentRepository;
import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.domain.Member;
import com.myproject.simpleboard.domain.member.domain.MemberRole;
import com.myproject.simpleboard.domain.member.domain.MemberStatus;
import com.myproject.simpleboard.domain.member.domain.Status;
import com.myproject.simpleboard.domain.post.dao.PostRepository;
import com.myproject.simpleboard.domain.post.domain.Post;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestData {
    private final Init init;


    // @PostConstruct
    public void init(){

        init.save();
    }

    @RequiredArgsConstructor
    @Component
    private static class Init{

        private final MemberRepository memberRepository;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;

        @Transactional
        public void save() {

            for(int i = 1; i <= 50; i++) {
                memberRepository.save(new Member("익명" + i, "1234", MemberRole.USER, new MemberStatus(Status.NORMAL)));
            }

            for(Long i = 1L; i <= 50; i++ ){
                Post post = new Post(i + " 번째 게시물", i + "번째 게시물 내용",
                    memberRepository.findById(i).get());
                postRepository.save(post);
            }

            for(Long i = 1L; i<=150; i++ ){
                Comment comment = new Comment("댓글" + i, memberRepository.findById(i%50 + 1).get(), postRepository.findById((NumberUtils.convertNumberToTargetClass(i%50 + 1, Long.class))).orElse(null));
                commentRepository.save(comment);
            }
        }
    }
}

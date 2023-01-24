package com.myproject.simpleboard.global.data;

import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import com.myproject.simpleboard.comment.Comment;
import com.myproject.simpleboard.comment.CommentRepository;
import com.myproject.simpleboard.global.common.customtype.Writer;
import com.myproject.simpleboard.post.Post;
import com.myproject.simpleboard.post.PostRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestData {
    private final Init init;


    @PostConstruct
    public void init(){

        init.save();
    }

    @RequiredArgsConstructor
    @Component
    private static class Init{

        private final PostRepository postRepository;
        private final CommentRepository commentRepository;

        @Transactional
        public void save() {

            for(int i = 0; i<=50; i++ ){
                Post post = new Post(i + " 번째 게시물", i + "번째 게시물 내용", new Writer("익명" + i));
                postRepository.save(post);
            }

            for(int i = 1; i<=150; i++ ){
                Comment comment = new Comment("댓글" + i, new Writer("익명"), postRepository.findById((NumberUtils.convertNumberToTargetClass(i%50, Long.class))).orElse(null));
                commentRepository.save(comment);
            }
        }
    }
}

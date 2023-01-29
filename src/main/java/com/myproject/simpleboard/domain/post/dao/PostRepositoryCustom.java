package com.myproject.simpleboard.domain.post.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.myproject.simpleboard.domain.post.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.myproject.simpleboard.domain.post.domain.QPost.*;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustom {
    
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Post> findByIdWithComments(Long id) {
        return Optional.ofNullable(
        jpaQueryFactory
        .select(post).distinct()
        .from(post)
        .leftJoin(post.comments)
        .leftJoin(post.images)
        .fetchJoin()
        .where(post.id.eq(id))
        .fetchOne()
        );
    }
}

package com.myproject.simpleboard.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PostRepositoryCustom {
    
    private final JPAQueryFactory jpaQueryFactory;


}

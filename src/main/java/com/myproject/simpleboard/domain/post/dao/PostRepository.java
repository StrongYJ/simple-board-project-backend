package com.myproject.simpleboard.domain.post.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.simpleboard.domain.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    
}

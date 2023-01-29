package com.myproject.simpleboard.domain.post.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.simpleboard.domain.post.domain.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    
}

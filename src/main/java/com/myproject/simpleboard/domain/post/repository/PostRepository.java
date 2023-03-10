package com.myproject.simpleboard.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.simpleboard.domain.post.entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select distinct p from Post p left join fetch p.images where p.id = :id")
    Optional<Post> findPostByIdWithImages(@Param("id") Long id);

    Page<Post> findByBoard(String Board, Pageable pageable);

    boolean existsByBoard(String board);
}

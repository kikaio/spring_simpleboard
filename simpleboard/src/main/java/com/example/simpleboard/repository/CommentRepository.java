package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

    @Override
    public List<Comment> findAll();

    @Override
    public Page<Comment> findAll(Pageable pageable);

    public Page<Comment> findByPost(Post post, Pageable pageable);
}

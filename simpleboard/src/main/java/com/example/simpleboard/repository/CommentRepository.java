package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

    @Override
    public List<Comment> findAll();
    
    public List<Comment> findByPost(Post post);
}

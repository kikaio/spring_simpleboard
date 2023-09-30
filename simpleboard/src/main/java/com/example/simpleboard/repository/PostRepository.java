package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    @Override
    public List<Post> findAll();
    
    public List<Post> findByBoard(Board board);
}

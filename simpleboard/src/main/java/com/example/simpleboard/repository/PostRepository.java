package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    @Override
    public Page<Post> findAll(Pageable pageable);

    @Override
    public List<Post> findAll();
    
    public Page<Post> findByBoard(Board board, Pageable pageable);
}

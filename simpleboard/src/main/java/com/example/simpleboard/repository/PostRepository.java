package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Post;

public interface PostRepository extends CrudRepository<Post, Long>
{
    @Override
    public List<Post> findAll();
    
    public List<Post> findByBoard_BoarId(long board_id);

    public List<Post> findByBoard(Board board);
}

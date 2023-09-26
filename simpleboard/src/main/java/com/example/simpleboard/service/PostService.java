package com.example.simpleboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Post;
import com.example.simpleboard.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    
    public PostService(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    }

    public List<Post> getPostsUsingBoard(Board board)
    {
        return postRepository.findByBoard(board);
    }

}

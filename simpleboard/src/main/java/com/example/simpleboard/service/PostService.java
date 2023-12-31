package com.example.simpleboard.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Post;
import com.example.simpleboard.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    
    public PostService(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    }

    public Page<Post> getPosts(Pageable pageable)
    {
        return postRepository.findAll(pageable);
    }

    public List<Post> getAllPosts()
    {
        return postRepository.findAll();
    }

    public Page<Post> getPostsUsingBoard(Board board, Pageable pageable)
    {
        return postRepository.findByBoard(board, pageable);
    }

    public boolean createPost(Post newPost)
    {
        try {
            var saved = postRepository.save(newPost);
            log.info("saved post : ".formatted(saved.toString()));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Post getPost(long id)
    {
        var post = postRepository.findById(id).orElse(null);
        return post;
    }

    public boolean updatePost(long id , Post newPost)
    {
        Post old = postRepository.findById(id).orElse(null);
        if(old == null)
        {
            log.info("post[%d] not exist in repo".formatted(id));
            return false;
        }
        old.Update(newPost);
        try {
            postRepository.save(old);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deletePost(Post post)
    {
        try {
            postRepository.delete(post);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

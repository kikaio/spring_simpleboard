package com.example.simpleboard.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Post;
import com.example.simpleboard.repository.CommentRepository;

@Service
public class CommentService {
 
    private final CommentRepository commentRepository;


    public CommentService(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getComments()
    {
        return commentRepository.findAll();
    }

    public Page<Comment> getCommentsUsingPost(Post post, Pageable pageable)
    {
        return commentRepository.findByPost(post, pageable);
    }

    public Comment getComment(long id)
    {
        return commentRepository.findById(id).orElse(null);
    }

    public boolean createComment(Comment comment)
    {
        try {
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateComment(Comment newComment)
    {
        try {
            Comment old = commentRepository.findById(newComment.getId()).orElse(null);
            if(old == null)
            {
                return false;
            }
            old.Update(newComment);
            commentRepository.save(old);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deleteComment(long id)
    {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

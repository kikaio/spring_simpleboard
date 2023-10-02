package com.example.simpleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simpleboard.dto.CommentDto;
import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Post;
import com.example.simpleboard.service.CommentService;
import com.example.simpleboard.service.PostService;

@Controller
@RequestMapping("/comments")
public class CommentController 
{
    private final PostService postService;
    private final CommentService commentService;
    
    public CommentController(PostService postService, CommentService commentService)
    {
        this.postService = postService;
        this.commentService = commentService;
    }

    //현재 Post에 속한 comment는 PostController에서 Get 하고 있는 상태.

    @PostMapping("/create")
    public String createComment(CommentDto commentDto, Model model)
    {
        if(commentDto.getPostId() == null)
        {
            //todo : invalid case
        }
        Post post = postService.getPost(commentDto.getPostId());
        if(post == null)
        {
            //todo : already deleted post, send to error page.
            return "";
        }
        Long post_id = commentDto.getPostId();
        Comment parentComment = null;

        if(commentDto.getParentId() != null)
        {
            parentComment = commentService.getComment(commentDto.getParentId());
            if(parentComment != null)
            {
                commentDto.setParentLive(true);
            }
        }

        var comment = commentDto.toEntity(post, parentComment);
        boolean success = commentService.createComment(comment);
        if(success == false)
        {
            //todo : send to error page
            return "";
        }
        //todo : 해당 post로 다시 redirect
        return "redirect:/posts/%d".formatted(post_id);
    }

    @PostMapping("/{id}")
    public String updateComment(CommentDto commentDto)
    {
        //갱신 시 post, 부모 comment에 대한 정보는 변경할 수 없다.
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable(required = true) long id)
    {
        return "";
    }
}

package com.example.simpleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.simpleboard.dto.PostDto;

@RequestMapping("/posts")
@Controller
public class PostController {
    
    @GetMapping("/{{id}}")
    public String getPost(@PathVariable(name = "id", required = true) long id)
    {
        return "";
    }

    @GetMapping("/create")
    public String createPostPage(@RequestParam(name = "board_id", required = true) long baord_id, Model model)
    {
        //todo : create page render, 
        model.addAttribute("board_id", baord_id);
        return "";
    }

    @PostMapping("/create")
    public String createPost(PostDto postDto)
    {
        return "";
    }

    @GetMapping("/update")
    public String updatePostPage(
        @RequestParam(name = "id", required = true) Long id
        , Model model
    )
    {
        //todo : find and send post dto
        return "";
    }

    @PostMapping("/{{id}}")
    public String updatePost(
        PostDto postDto
    )
    {
        return "";        
    }

    @DeleteMapping("/{{id}}")
    public String deletePost()
    {
        //todo : delete post and comments.
        return "";
    }
}

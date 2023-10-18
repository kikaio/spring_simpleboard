package com.example.simpleboard.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.simpleboard.dto.CommentDto;
import com.example.simpleboard.dto.PostDto;
import com.example.simpleboard.dto.SimplePageDto;
import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Post;
import com.example.simpleboard.service.BoardService;
import com.example.simpleboard.service.CommentService;
import com.example.simpleboard.service.PostService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/posts")
@Controller
@Slf4j
public class PostController {
    
    private final PostService postService;
    private final BoardService boardService;
    private final CommentService commentService;

    private static final int cntPerPage = 5;


    public PostController(
        PostService postService
        , BoardService boardService
        , CommentService commentService
    )
    {
        this.postService = postService;
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @GetMapping("/test")
    public String test()
    {
        var boards = boardService.getBoards();
        boards.forEach(board->{
            for(int idx = 0; idx < 5; idx++)
            {
                var post = Post.builder()
                    .title("test title_%d".formatted(idx+1))
                    .board(board)
                    .content("test content")
                    .build()
                ;
                postService.createPost(post);
            }
        });
        return "redirect:/posts";
    }

    @GetMapping("")
    public String getPosts(
        @RequestParam(name="page", defaultValue = "0") int pageIdx
        , Model model
    ) throws Exception
    {
        //all post get.
        var pageable = PageRequest.of(pageIdx, cntPerPage);
        
        var posts = postService.getPosts(pageable);
        var postsDtos = posts.map(ele->new PostDto(ele));
        var pageDto = new SimplePageDto<Page>(postsDtos);
        model.addAttribute("posts", postsDtos.toList());
        model.addAttribute("pageDto", pageDto);
        return "/posts/posts";
    }

    @GetMapping("/{id}")
    public String getPost(
        @PathVariable(name = "id", required = true) long id
        , @RequestParam(name="page", defaultValue = "0") int pageIdx
        , Model model
    ) throws Exception
    {
        var commentPageable = PageRequest.of(pageIdx, cntPerPage);

        var post = postService.getPost(id);
        if(post == null)
        {
            throw new Exception("post[%d] not exist in repo".formatted(id));
        }   

        var board = post.getBoard();
        if(board == null)
        {
            throw new Exception("post[%d]'s board' not exist in repo".formatted(id));
        }

        var board_id = board.getId();
        var board_name = board.getName();
        
        var postDto = new PostDto(post);
        var comments = commentService.getCommentsUsingPost(post, commentPageable);
        var commentDtos = CommentDto.calcCommentsChilds(comments.toList(), false);
        var commentPageDto = new SimplePageDto<>(comments);

        model.addAttribute("board_id", board_id);
        model.addAttribute("board_name", board_name);
        model.addAttribute("post", postDto);
        model.addAttribute("comments", commentDtos);
        model.addAttribute("commentPageDto", commentPageDto);
        return "/posts/post";
    }

    @GetMapping("/create")
    public String createPostPage(
        @RequestParam(name = "board_id", required = true) long baord_id
        , @RequestParam(name = "board_name", required = true) String baord_name, Model model
    )
    {
        //todo : create page render, 
        model.addAttribute("board_id", baord_id);
        model.addAttribute("board_name", baord_name);
        return "/posts/post_create";
    }

    @PostMapping("/create")
    public String createPost(PostDto postDto) throws Exception
    {
        if(postDto.getBoard_id() == null)
            throw new Exception("board id must have value");

        long board_id = postDto.getBoard_id();
        Board board = boardService.getBoard(board_id);
        if(board == null)
        {
            throw new Exception("board[%d] is not exist in boards".formatted(board_id));
        }
        //todo : valid check title, contents, etc
        Post newPost = postDto.toEntity(board);
        newPost.setBoard(board);
        if(postService.createPost(newPost) == false)
        {
            throw new Exception("post create to board[%d] failed.".formatted(board_id));
        }
        return "redirect:/boards/%d".formatted(board_id);
    }

    @GetMapping("/update")
    public String updatePostPage(
        @RequestParam(name = "id", required = true) long id
        , Model model
    ) throws Exception
    {
        //todo : find and send post dto
        Post post = postService.getPost(id);
        if(post == null)
        {
            throw new Exception("post[%d] not exist in repo".formatted(id));
        }
        model.addAttribute("post", post);
        return "/posts/post_update";
    }

    @PostMapping("/{id}")
    public String updatePost(
        @PathVariable(name = "id", required = true) long id
        , PostDto postDto
    ) throws Exception
    {
        if(postDto.getId() != null && id != postDto.getId())
        {
            throw new Exception("invalid request.");
        }

        Long board_id = postDto.getBoard_id();
        if(board_id == null)
        {
            throw new Exception("board id must not null");
        }
        Board board = boardService.getBoard(board_id);
        Post post = postDto.toEntity(board);
        post.setBoard(board);
        if(postService.updatePost(id, post) == false)
        {
            log.info("post[%d] update failed".formatted(id));
        }
        return "redirect:/posts/%d?board_id=%d".formatted(id, board_id);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deletePost(
        @PathVariable(name = "id", required = true) long id
     ) throws Exception
    {
        Post post = postService.getPost(id);
        if(post == null)
        {
            throw new Exception("post[%d] not exist in repo".formatted(id));
        }
        if(post.getBoard() == null)
        {
            throw new Exception("post[%d] not assigned board, something invalid".formatted(id));
        }

        long board_id = post.getBoard().getId();
        if(postService.deletePost(post) == false)
        {
            throw new Exception("post[%d] delete failed, something invalid".formatted(id));
        }
        else
        {
            log.info("post[title : %s] was deleted.".formatted(post.getTitle()));
        }
        //todo : delete post and comments.
        var modelAndView = new ModelAndView("redirect:/boards/%d".formatted(board_id));
        modelAndView.setStatus(HttpStatus.SEE_OTHER);
        return modelAndView;
    }
}

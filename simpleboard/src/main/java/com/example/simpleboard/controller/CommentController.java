package com.example.simpleboard.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.data.util.Pair;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.simpleboard.dto.CommentDto;
import com.example.simpleboard.dto.CommentListDto;
import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Post;
import com.example.simpleboard.service.BoardService;
import com.example.simpleboard.service.CommentService;
import com.example.simpleboard.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/comments")
public class CommentController 
{
    private final PostService postService;
    private final CommentService commentService;
    private final BoardService boardService;

    public CommentController(
        BoardService boardService,
        PostService postService, CommentService commentService
    )
    {
        this.boardService = boardService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/test")
    public String test()
    {
        boardService.createBoard("testBoard");
        Board board = boardService.getBoard(1L);
        Post post = Post.builder()
            .board(board)
            .title("test title")
            .content("test post content str")
            .build()
        ;
        postService.createPost(post);
        post = postService.getPost(1L);

        var parent = Comment.builder()
            .comment("test comment")
            .post(post)
            .build()
        ;
        commentService.createComment(parent);

        Comment child = Comment.builder()
            .comment("this is child")
            .parent(parent)
            .parentRefId(parent.getId())
            .post(post)
            .build()
        ;
        commentService.createComment(child);

        return "redirect:/comments";
    }

    @GetMapping("")
    public String getAllComments(Model model)
    {
        var comments = commentService.getComments();
        var commentDtos = CommentDto.calcCommentsChilds(comments);
        HashMap<Long, Pair<Long, String>> cacheMap = new HashMap<>();
        HashMap<Long, ArrayList<CommentDto>> commentDtoCache = new HashMap<>();

        comments.forEach(entity->{
            var postEntity = entity.getPost();
            if (postEntity == null || postEntity.getBoard() == null)
            {
                return ;
            }
            var board = postEntity.getBoard();
            var postId = postEntity.getId();
            var postTitle = postEntity.getTitle();
            cacheMap.put(postId, Pair.of(board.getId(), postTitle));
        });

        commentDtos.forEach(dto->{
            Long postId = dto.getPostId();
            if(commentDtoCache.containsKey(postId) == false)
            {
                commentDtoCache.put(postId, new ArrayList<>());
            }
            var value = commentDtoCache.get(postId);
            value.add(dto);
        });

        ArrayList<CommentListDto> retModel = new ArrayList<>();
        cacheMap.forEach((postId, val)->{
            CommentListDto dto = new CommentListDto(postId, val.getFirst(), val.getSecond(), commentDtoCache.get(postId));
            retModel.add(dto);
        });

        model.addAttribute("commentContainer", retModel);
        return "/comments/comments";
    }

    //현재 Post에 속한 comment는 PostController에서 Get 하고 있는 상태.

    @PostMapping("/create")
    public String createComment(CommentDto commentDto, Model model, RedirectAttributes reAttr)
    {
        if(commentDto.getPostId() == null)
        {
            //todo : invalid case
            return "";
        }
        Post post = postService.getPost(commentDto.getPostId());
        if(post == null)
        {
            //todo : already deleted post, send to error page.
            return "";
        }
        Long post_id = commentDto.getPostId();
        Long board_id = post.getBoard().getId();
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
        if(commentService.createComment(comment) == false)
        {
            //todo : send to error page
            return "";
        }
        log.info("redirect:/posts/%d".formatted(post_id));
        reAttr.addAttribute("board_id", board_id);
        //todo : 해당 post로 다시 redirect
        return "redirect:/posts/%d".formatted(post_id);
    }

    @PostMapping("/{id}")
    public String updateComment(CommentDto commentDto, RedirectAttributes reAddr)
    {
        //갱신 시 post, 부모 comment에 대한 정보는 변경할 수 없다.
        Long comment_id = commentDto.getId();

        //comment의 경우 변경되는 data는 comment str만 한다고 가정한다.
        var comment = commentService.getComment(comment_id);
        if(comment == null)
        {
            //todo : error page
            return "";
        }
        Long parent_id = comment.getParentRefId();
        var post = comment.getPost();
        Long post_id = post.getId();
        Long board_id = post.getBoard().getId();

        if(commentDto.getParentId() != parent_id)
        {
            //todo : error page
            return "";
        }
        if(commentDto.getPostId() != post_id)
        {
            //todo : error page
            return "";
        }

        if(false)
        {
            //todo : author or admin check 
            return "";
        }

        var newComment = commentDto.toEntity(post, comment.getParent());
        commentService.updateComment(newComment);

        reAddr.addAttribute("board_id", board_id);
        return "redirect:/posts/%d".formatted(post_id);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteComment(
        @PathVariable(name = "id", required = true) long id
        , @RequestParam(name = "board_id", required = true) Long board_id
        , @RequestParam(name = "post_id", required = true) Long post_id
        , RedirectAttributes reAttr
    )
    {
        var comment = commentService.getComment(id);
        if(comment == null)
        {
            //todo: error page, already deleted commment
            reAttr.addAttribute("board_id", board_id);
            var ret = new ModelAndView("redirect:/posts/%d".formatted(post_id), HttpStatus.SEE_OTHER);
            return ret;
        }
        var post = comment.getPost();
        if(post == null)
        {
            //laready post deleted, just delete comment and redirect to board detail page
            commentService.deleteComment(id);
            var ret = new ModelAndView("redirect:/boards/%d".formatted(board_id), HttpStatus.SEE_OTHER);
            return ret;
            
        }
        if(commentService.deleteComment(id) == false)
        {
            //todo : error page
            return new ModelAndView("");
        }

        reAttr.addAttribute("board_id", board_id);
        var ret = new ModelAndView("redirect:/posts/%d".formatted(post_id), HttpStatus.SEE_OTHER);
        return ret;
    }
}

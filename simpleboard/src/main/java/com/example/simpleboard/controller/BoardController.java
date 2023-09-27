package com.example.simpleboard.controller;


import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;

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

import com.example.simpleboard.dto.BoardDto;
import com.example.simpleboard.dto.PostDto;
import com.example.simpleboard.entity.Board;
import com.example.simpleboard.service.BoardService;
import com.example.simpleboard.service.PostService;

import lombok.extern.slf4j.Slf4j;


@RequestMapping("/boards")
@Controller
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;

    public BoardController(
        BoardService boardService
        , PostService postService
    )
    {
        this.boardService = boardService;
        this.postService = postService;
    }

    @GetMapping()
    public String getBoards(Model model)
    {
        List<BoardDto> boards = new ArrayList<BoardDto>();
        List<Board> boardEntities = boardService.getBoards();
        for(var entity : boardEntities)
        {
            boards.add(new BoardDto(entity));
        }
        model.addAttribute("boards", boards);
        return "boards/boards";
    }

    @GetMapping("/{id}")
    public String getBoard(
        @PathVariable(name = "id", required = true) long id
        , Model model
        ) throws Exception
    {
        //todo : get board, post, etc using DTO
        Board target = boardService.getBoard(id);
        if(target == null)
            throw new Exception("not exist board[%d].".formatted(id));

        BoardDto targetDto = new BoardDto(target);
        model.addAttribute("board", targetDto);
        //todo : model include posts.
        List<PostDto> postsDto = new ArrayList<>();
        var posts = postService.getPostsUsingBoard(target);
        for(var post : posts)
        {
            postsDto.add(new PostDto(post));
        }
        model.addAttribute("posts", postsDto);
        return "boards/board";
    }

    @GetMapping("/create")
    public String createBoardPage()
    {
        //todo : move to create board page
        return "boards/board_create";
    }

    @PostMapping("/create")
    public String createBoard(
        @RequestParam(name="name", required = true) String name
        )
    {
        boolean success = boardService.createBoard(name);
        if(success)
        {
            log.info("board[%s] created".formatted(name));
        }
        else
        {
            log.info("board[%s] create was failed".formatted(name));
        }
        return "redirect:/boards";
    }

    @GetMapping("/update")
    public String updateBoardPage(
        @RequestParam(name = "id", required = true) Long id
        , Model model
        ) throws Exception
    {
        Board target = boardService.getBoard(id);
        if(target == null)
            throw new NameNotFoundException();

        BoardDto targetDto = new BoardDto(target);
        model.addAttribute("board", targetDto);
        return "boards/board_update";
    }

    @PostMapping("/{board_id}")
    public String updateBoard(
        @PathVariable(name = "board_id", required = true) long board_id
        , BoardDto dto
        )
    {
        boolean successed = boardService.updateBoard(board_id, dto.toEntity());
        if(successed)
        {
            log.info("board[%d:%s] was updated".formatted(board_id, dto.getName()));
        }
        else
        {
            log.info("board[%d:%s] update failed, maybe not exist in repo".formatted(board_id, dto.getName()));
        }
        return "redirect:/boards";
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteBaord(
        @PathVariable(name = "id", required = true) long id
        )
    {
        boolean success = boardService.deleteBoard(id);
        if(success)
        {
            log.info("Board[%d] deleted.".formatted(id));
        }
        else
        {
            log.info("Board[%d] delete failed, maybe not exist in repo.".formatted(id));
        }

        var modelAndView = new ModelAndView("redirect:/boards");
        modelAndView.setStatus(HttpStatus.SEE_OTHER);
        return modelAndView;
    }
}

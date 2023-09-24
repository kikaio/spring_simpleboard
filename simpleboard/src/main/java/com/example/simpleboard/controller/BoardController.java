package com.example.simpleboard.controller;


import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;

import javax.naming.NameNotFoundException;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@RequestMapping("/boards")
@Controller
@Slf4j
public class BoardController {
    
    @AllArgsConstructor
    @ToString
    class Board{
        public long id;
        public String name;
    }

    ArrayList<Board> boards = new ArrayList<>();
    @PostConstruct
    public void init()
    {
        for(int idx = 0; idx < 5; idx++)
        {
            boards.add(new Board(idx, "b_%d".formatted(idx)));
        }
    }

    @GetMapping()
    public String getBoards(Model model)
    {
        model.addAttribute("boards", boards);
        //todo : get boards list
        return "boards/boards";
    }

    @GetMapping("/{id}")
    public String getBoard(
        @PathVariable(name = "id", required = true) long id
        , Model model
        )
    {
        //todo : get board, post, etc using DTO
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
        BoardController.Board board =new Board((long)boards.size(), name); 
        boards.add(board);
        //todo : create board using name., redirect to getBoards
        return "redirect:/boards";
    }

    @GetMapping("/update")
    public String updateBoardPage(
        @RequestParam(name = "id", required = true) Long id
        , Model model
        ) throws Exception
    {
        Board target = null;
        for(var board : boards)
        {
            if(board.id == id)
            {
                target = board;
                break;
            }
        }

        if(target == null)
        {
            throw new NameNotFoundException();
        }
        else
        {
            model.addAttribute("board", target);
        }
        //todo : set model data
        return "boards/board_update";
    }

    @PostMapping("/{id}")
    public String updateBoard(
        @PathVariable(name = "id", required = true) long id
        , @RequestParam(name = "name", required = true) String name
        ) throws Exception
    {
        //todo : update board's name using request param. => someday change to DTO
        Board target = null;
        for(var board : boards)
        {
            if(board.id == id)
            {
                target = board;
                break;
            }
        }

        if(target == null)
        {
            throw new NameNotFoundException();
        }
        else
        {
            target.name = name;
        }
        return "redirect:/boards";
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteBaord(
        @PathVariable(name = "id", required = true) long id
        , HttpServletRequest request
        , HttpServletResponse response
        )
    {
        // todo : delete board and posts, etc.
        for(int idx = 0; idx < boards.size(); ++idx)
        {
            Board target = boards.get(idx);
            if(target.id == id)
            {
                boards.remove(target);
                break;
            }
        }
        log.info("method is %s".formatted(request.getMethod()));
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.SEE_OTHER);
        return new ModelAndView("redirect:/boards");
    }
}

package com.example.simpleboard.controller;


import java.util.ArrayList;

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

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@RequestMapping("/boards")
@Controller
@Slf4j
public class BoardController {

    ArrayList<BoardDto> boards = new ArrayList<>();
    @PostConstruct
    public void init()
    {
        for(int idx = 0; idx < 5; idx++)
        {
            boards.add(new BoardDto((long)idx, "b_%d".formatted(idx)));
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
        ) throws Exception
    {
        //todo : get board, post, etc using DTO
        BoardDto target = null;
        for(int idx = 0; idx < boards.size(); ++idx)
        {
            if(boards.get(idx).getId() == id)
            {
                target= boards.get(idx);
                break;
            }
        }
        if(target == null)
        {
            throw new Exception("not exist board[%llu].".formatted(id));
        }

        model.addAttribute("board", target);
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
        BoardDto board =new BoardDto((long)boards.size(), name); 
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
        BoardDto target = null;
        for(var board : boards)
        {
            if(board.getId() == id)
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
        BoardDto target = null;
        for(var board : boards)
        {
            if(board.getId() == id)
            {
                target = new BoardDto(id, name);
                break;
            }
        }

        if(target == null)
        {
            throw new NameNotFoundException();
        }
        return "redirect:/boards";
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteBaord(
        @PathVariable(name = "id", required = true) long id
        )
    {
        // todo : delete board and posts, etc.
        for(int idx = 0; idx < boards.size(); ++idx)
        {
            BoardDto target = boards.get(idx);
            if(target.getId() == id)
            {
                boards.remove(target);
                break;
            }
        }
        var modelAndView = new ModelAndView("redirect:/boards");
        modelAndView.setStatus(HttpStatus.SEE_OTHER);
        return modelAndView;
    }
}

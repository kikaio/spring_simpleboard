package com.example.simpleboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/boards")
@Controller
public class BoardController {
    
    @GetMapping()
    public String getBoards(Model model)
    {
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
        @RequestParam("name") String name, Model model
        )
    {
        //todo : create board using name., redirect to getBoards
        return "redirect:/boards";
    }

    @GetMapping("/update")
    public String updateBoardPage(
        @RequestParam(name = "id", required = true) long id
        , @RequestParam(name="name", required = true) String name
        , Model model
        )
    {
        //todo : set model data
        return "boards/boad_update";
    }

    @PostMapping("/{id}")
    public String updateBoard(
        @PathVariable(name = "id", required = true) long id
        , @RequestParam(name = "name", required = true) String name
        )
    {
        //todo : update board's name using request param. => someday change to DTO
        return "redirect:/boards";
    }

    @DeleteMapping("/{id}")
    public String deleteBaord(
        @PathVariable(name = "id", required = true) long id
        )
    {
        // todo : delete board and posts, etc.
        return "redirect:/boards";
    }
}

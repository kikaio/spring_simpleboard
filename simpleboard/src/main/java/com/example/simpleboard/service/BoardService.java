package com.example.simpleboard.service;

import org.springframework.stereotype.Service;

import com.example.simpleboard.repository.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository)
    {
        this.boardRepository = boardRepository;
    }

    public boolean createBoard(String name)
    {
        return true;
    }

    public boolean updateBoard(long id, String name)
    {
        return true;
    }

    public boolean deleteBoard(long id)
    {
        return true;
    }
    
}

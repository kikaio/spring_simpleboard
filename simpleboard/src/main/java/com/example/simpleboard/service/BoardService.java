package com.example.simpleboard.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Board;
import com.example.simpleboard.repository.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository)
    {
        this.boardRepository = boardRepository;
    }

    public List<Board> getBoards()
    {
        return boardRepository.findAll();
    }

    public Board getBoard(long id)
    {
        Optional<Board> target = boardRepository.findById(id);
        //orElse, orElseGet 차이점 주의.
        var ret = target.orElse(null);
        return ret;
    }

    public boolean createBoard(String name)
    {
        Board target = Board.builder().id(null).name(name).build();
        try
        {
            boardRepository.save(target);
        }
        catch(IllegalArgumentException e)
        {
            //maybe memory issue?
        }
        catch(OptimisticEntityLockException e)
        {
            //
            return false;
        }
        return true;
    }

    public boolean updateBoard(long id, Board entity)
    {
        var target = getBoard(id);
        if(target == null)
        {
            return false;
        }

        try 
        {
            target.Update(entity);
            boardRepository.save(target);
        } 
        catch(OptimisticLockingFailureException e)
        {
            //아마도 target 획득 시에는 존재했는데 save 당시에는 없어진경우
            return false;
        }
        return true;
    }

    public boolean deleteBoard(long id)
    {
        var target = getBoard(id);
        //없는데 요청 온것도 false 처리.
        if(target == null)
            return false;

        try 
        {
            boardRepository.delete(target);
        } 
        catch(OptimisticLockingFailureException e)
        {
            //target 획득 시에는 존재했는데 save 당시에는 없어진경우는 없어졌기 때문에 true 처리.
            return true;
        }
        return true;
    }

}

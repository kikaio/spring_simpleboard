package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Board;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
    @Override
    public List<Board> findAll();
}

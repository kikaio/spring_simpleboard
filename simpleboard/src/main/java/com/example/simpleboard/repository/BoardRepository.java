package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Override
    public List<Board> findAll();
}

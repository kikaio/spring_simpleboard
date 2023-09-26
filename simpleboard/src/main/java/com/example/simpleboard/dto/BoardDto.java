package com.example.simpleboard.dto;

import com.example.simpleboard.entity.Board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class BoardDto {
    private Long id;
    private String name;

    public BoardDto(Board board)
    {
        fromEntity(board);
    }
    
    public Board toEntity()
    {
        return Board.builder()
            .id(id)
            .name(name)
            .build();
    }

    public void fromEntity(Board board)
    {
        this.id = board.getId();
        this.name = board.getName();
    }
}

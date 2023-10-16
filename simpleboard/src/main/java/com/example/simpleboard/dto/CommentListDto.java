package com.example.simpleboard.dto;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Component
@Setter
@Getter
public class CommentListDto {
    private Long postId;
    private Long boardId;
    private String postTitle;
    private ArrayList<CommentDto> comments;
}
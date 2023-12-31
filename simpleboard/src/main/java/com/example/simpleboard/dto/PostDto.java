package com.example.simpleboard.dto;

import com.example.simpleboard.entity.Board;
import com.example.simpleboard.entity.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class PostDto {

    private Long id;

    private String title;
    
    private String content;

    private Long board_id;

    private String board_name;

    public PostDto(Post post)
    {
        fromEntity(post);
    }

    public Post toEntity(Board board)
    {
        if(board == null)
        {
            return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .build()
            ;
        }
        else {
            return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .board(board)
                .build()
            ;
        }
    }

    public void fromEntity(Post post)
    {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        if(post.getBoard() != null)
        {
            board_id = post.getBoard().getId();
            board_name = post.getBoard().getName();
        }
        return ;
    }
}

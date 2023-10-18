package com.example.simpleboard.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.example.simpleboard.entity.Comment;
import com.example.simpleboard.entity.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Component
@ToString
public class CommentDto {
    
    private Long id;
    private Long boardId;
    private Long postId;
    private Long parentId;

    private String comment;
    private boolean isDeleted = false;
    private boolean isParentLive = false;

    private boolean isJustRender = false;

    private List<CommentDto> childs = new ArrayList<>();

    public CommentDto(Comment comment)
    {
        fromEntity(comment);
    }

    public void fromEntity(Comment comment)
    {
        this.id = comment.getId();
        if(comment.getPost() != null)
        {
            this.postId = comment.getPost().getId();
            this.boardId = comment.getPost().getBoard().getId();
        }
        else 
        {
            this.postId = null;
            this.boardId = null;
        }

        this.comment = comment.getComment();
        this.parentId = comment.getParentRefId();

        if(this.parentId != null)
        {
            this.isParentLive = comment.getParent() == null? false : true;
        }
        else
        {
            this.isParentLive = false;
        }
    }

    public Comment toEntity(Post post, Comment parent)
    {
        return Comment.builder()
            .id(this.id)
            .post(post)
            .parent(parent)
            .parentRefId(this.parentId)
            .comment(this.comment)
            .build()
        ;
    }

    private void addChild(CommentDto child)
    {
        childs.add(child);
    } 

    public static ArrayList<CommentDto> calcCommentsChilds(List<Comment> entities, boolean isJustRender)
    {
        var dtos = new ArrayList<CommentDto>();
        TreeMap<Long, CommentDto> parentMap = new TreeMap<>();
        TreeMap<Long, CommentDto> childMap = new TreeMap<>();

        entities.forEach(ele->{
            CommentDto dto = new CommentDto(ele); 
            dto.setJustRender(isJustRender);
            if(dto.getParentId() != null)
            {
                childMap.put(dto.getId(), dto);
            }
            else
            {
                parentMap.put(dto.getId(), dto);
            }
        });

        childMap.forEach((key, dto)->{
            Long parentId = dto.getParentId();
            var parentComment = parentMap.get(parentId);
            if(parentComment == null && parentMap.containsKey(parentId) == false)
            {
                CommentDto parent = new CommentDto(
                    parentId
                    , dto.getBoardId()
                    , dto.getPostId()
                    , null
                    , null
                    , true
                    , false
                    , false
                    , new ArrayList<CommentDto>()
                );
                parentMap.put(parentId, parent);
                parentComment = parent;
            }
            if(parentComment != null)
            {
                parentComment.addChild(dto);
            }
        });

        parentMap.forEach((key, dto)->{
            dtos.add(dto);
        });

        return dtos;
    }
}

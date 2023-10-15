package com.example.simpleboard.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="post_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true, unique = false)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Comment parent;

    @Column
    @Nullable
    private Long parentRefId;

    public void setPost(Post post)
    {
        this.post = post;
    }

    public void setParent(Comment parent)
    {
        this.parent = parent;
        if(parent != null)
        {
            this.parentRefId = parent.getId();
        }
    }

    public void Update(Comment other)
    {
        if(other.comment != null)
        {
            this.comment = other.comment;
        }
        //댓글이 달린 게시글정보를 변경할 일이 없으므로 갱신 대상에서 post는 제외.
        //대댓글이 달린 댓글 대상을 변경할 일이 없으므로 갱신 대상에서 post는 제외.
        //todo : mdate 추후에 추가시 수정.
    }
}

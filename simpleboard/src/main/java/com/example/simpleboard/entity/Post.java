package com.example.simpleboard.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.example.simpleboard.entity.utils.BaseUtcTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@ToString
public class Post extends BaseUtcTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "board_id", referencedColumnName = "id"
        , foreignKey = @ForeignKey(name="fk_board_id")
    )
    @ToString.Exclude
    @Setter
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    public void Update(Post other)
    {
        if(other.title != null && this.title != other.title)
        {
            this.title= other.title;
        }
        if(other.content != null && this.content != other.content)
        {
            this.content = other.content;
        }
    }

    @Column
    @Setter
    @CreatedDate
    private LocalDateTime createdDate;

    @Column
    @Setter
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}

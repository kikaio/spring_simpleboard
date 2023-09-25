package com.example.simpleboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    public void Update(Board other)
    {
        //id의 경우 null인 항목에 update를 할 일이 없고 PK 값이므로 아예 제외. 
        // if(other.id != null && this.id != other.id)
        //     this.id = other.id;
        
        if(other.name != null && this.name != other.name)
            this.name = other.name;
    }
}

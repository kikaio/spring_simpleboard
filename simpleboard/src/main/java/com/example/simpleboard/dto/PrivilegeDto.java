package com.example.simpleboard.dto;

import org.springframework.stereotype.Component;

import com.example.simpleboard.entity.Privilege;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
@Getter
@Setter
@ToString
public class PrivilegeDto {
    private Long id;
    private String name = "";
    private String desc = "";

    public PrivilegeDto(Privilege privilege)
    {
        fromEntity(privilege);
    }

    public void fromEntity(Privilege privilege)
    {
        if(privilege == null)
        {
            return ;
        }
        this.id = privilege.getId();
        this.name = privilege.getName();
        this.desc = privilege.getDesc();
    }

    public Privilege toEntity()
    {
        return Privilege.builder()
            .id(id)
            .name(name)
            .desc(desc)
            .build()
        ;
    }
}

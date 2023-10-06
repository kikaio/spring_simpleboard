package com.example.simpleboard.dto;

import org.springframework.stereotype.Component;

import com.example.simpleboard.entity.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
@ToString
public class RoleDto {

    private Long id;
    private String name;
    private String desc = "";

    public RoleDto(Role role)
    {
        fromEntity(role);
    }

    public void fromEntity(Role role)
    {
        id = role.getId();
        name = role.getName();
        desc = role.getDesc();
    }

    public Role toEntity()
    {
        return Role.builder()
            .id(id)
            .name(name)
            .desc(desc)
            .build()
        ;
    }
}

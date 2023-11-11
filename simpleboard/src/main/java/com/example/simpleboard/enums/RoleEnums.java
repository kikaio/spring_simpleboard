package com.example.simpleboard.enums;

public enum RoleEnums {
    //essential
    ROLE_USER("role for basic user "), 
    ROLE_ADMIN("role for admin or manager");
    public final String descRole;

    private RoleEnums(String desc)
    {
        this.descRole = desc;
    }
}

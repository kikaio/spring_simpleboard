package com.example.simpleboard.entity;

import java.math.BigInteger;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(
    name="roleToAuthority"
)
public class RoleToAuthority 
{
    
    private BigInteger roleId;
    private BigInteger authorityId;
}

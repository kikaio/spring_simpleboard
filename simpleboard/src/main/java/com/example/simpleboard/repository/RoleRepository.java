package com.example.simpleboard.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    @Override
    public Page<Role> findAll(Pageable pageable);
}

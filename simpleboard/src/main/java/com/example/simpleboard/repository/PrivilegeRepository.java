package com.example.simpleboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
    
    @Override
    public List<Privilege> findAll();

    public Optional<Privilege> findByName(String name);
}

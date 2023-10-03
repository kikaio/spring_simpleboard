package com.example.simpleboard.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>
{
    @Override
    public ArrayList<Member> findAll();
    
    public Optional<Member> findByEmail(String email);
}

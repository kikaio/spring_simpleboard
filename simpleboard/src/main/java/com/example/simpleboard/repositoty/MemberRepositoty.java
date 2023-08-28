package com.example.simpleboard.repositoty;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleboard.entity.MemberEntity;

@Repository
public interface MemberRepositoty extends CrudRepository<MemberEntity, String>
{
    public Optional<MemberEntity> findByEmail(String email);

    public boolean existsByEmail(String email);
}

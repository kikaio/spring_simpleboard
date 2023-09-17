package com.example.simpleboard.repositoty;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.simpleboard.entity.AuthorityEntity;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, BigInteger>
{
    public List<AuthorityEntity> findAllByMemberId(BigInteger member_id);
}

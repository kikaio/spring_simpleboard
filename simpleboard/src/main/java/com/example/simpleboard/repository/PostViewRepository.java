package com.example.simpleboard.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.simpleboard.entity.redis.PostViewCnt;
import java.util.Optional;


public interface PostViewRepository extends CrudRepository<PostViewCnt, String>
{
    Optional<PostViewCnt> finByPostId(String postId);
}

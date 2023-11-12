package com.example.simpleboard.entity.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@Builder
@RedisHash(value="post_view_cnt")
public class PostViewCnt {

    @Id
    private String postId;

    private int viewCnt;

    @TimeToLive
    private long ttl;

    public PostViewCnt update(String postId, long ttl)
    {
        this.postId= postId;
        this.ttl = ttl;
        return this;
    }
}

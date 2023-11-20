package com.example.simpleboard.entity.utils;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public class BaseUtcTimeEntity {

    @CreationTimestamp
    protected OffsetDateTime createdDt;

    @UpdateTimestamp
    protected OffsetDateTime updatedDt;
}

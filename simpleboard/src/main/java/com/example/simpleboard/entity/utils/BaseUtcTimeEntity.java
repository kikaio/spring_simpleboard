package com.example.simpleboard.entity.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @PrePersist
    public void prePersist()
    {
//        this.createdDt = OffsetDateTime.now(ZoneId.of("UTC"));
        this.updatedDt = this.createdDt;
    }

    @PreUpdate
    public void preUpdate()
    {
  //      this.updatedDt = OffsetDateTime.now(ZoneId.of("UTC"));
    }
}

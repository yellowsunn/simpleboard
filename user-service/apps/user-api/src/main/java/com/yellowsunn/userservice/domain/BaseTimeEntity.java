package com.yellowsunn.userservice.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

    @PrePersist
    void prePersist() {
        var now = ZonedDateTime.now();
        this.createdAt = now;
        this.modifiedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }
}

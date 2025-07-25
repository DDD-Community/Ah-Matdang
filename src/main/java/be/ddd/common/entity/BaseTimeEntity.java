package be.ddd.common.entity;

import be.ddd.common.util.CustomClock;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseTimeEntity {

    @CreatedDate
    @Comment("생성 일자")
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private ZonedDateTime createdDateTime;

    @LastModifiedDate
    @Comment("수정 일자")
    @Column(name = "UPDATE_DATE")
    private ZonedDateTime modifiedDateTime;

    public BaseTimeEntity() {
        createdDateTime = modifiedDateTime = CustomClock.now();
    }

    @PrePersist
    public void prePersist() {
        createdDateTime = modifiedDateTime = CustomClock.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedDateTime = CustomClock.now();
    }
}

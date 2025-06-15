package be.ddd.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Comment("생성 일자")
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Comment("수정 일자")
    @Column(name = "UPDATE_DATE")
    private LocalDateTime modifiedDateTime;
}

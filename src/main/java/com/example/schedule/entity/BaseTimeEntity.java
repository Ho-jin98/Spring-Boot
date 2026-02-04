package com.example.schedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
//TODO 엔티티에 리스너를 연결해주는 어노테이션
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    //TODO main메서드에 @EnableJpaAuditing 어노테이션을 설정하면,save를 하는 순간
    // createdAt이 현재 시각을 기록하고, modifiedAt이 데이터가 처음 생겨난 것도 일종의 변경으로 간주하여
    // createdAt과 동일한 시각을 자동으로 채워줌

    //TODO @CreatedDate >> 엔티티의 생성 시간을 자동으로 관리해주는 어노테이션
    @CreatedDate
    @Column(updatable = false, nullable = false)
    //TODO createdAt >> 처음 생성된 시각을 자동으로 기록하는 필드(작성일)
    // 작성일은 수정이 불가능이기 때문에, updatable = false
    private LocalDateTime createdAt;

    //TODO @LastModifiedDate >> 엔티티의 수정 시간을 자동으로 관리해주는 어노테이션
    @LastModifiedDate
    @Column(nullable = false)
    //TODO modifiedAt >> 수정(업데이트)시점을 자동으로 기록하는 필드(수정일)
    // 만약 updatable = false를 걸게되면 수정이 불가능해짐
    private LocalDateTime modifiedAt;

}

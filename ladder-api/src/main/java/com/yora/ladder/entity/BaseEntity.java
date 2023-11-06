package com.yora.ladder.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Auditable<String, Long, LocalDateTime> {

     @Id
     @GeneratedValue
     private Long id;

     @Column(nullable = false, updatable = false)
     @CreatedDate
     private LocalDateTime createdDate;

     @Column
     @LastModifiedDate
     private LocalDateTime lastModifiedDate;

     @Column(nullable = false, updatable = false)
     @CreatedBy
     private String createdBy;

     @Column
     @LastModifiedBy
     private String lastModifiedBy;

     @Override
     public boolean isNew() {
          return Objects.isNull(id) || id <= 0;
     }

     @Override
     public Optional<LocalDateTime> getLastModifiedDate() {
          return Optional.ofNullable(this.lastModifiedDate);
     }

     @Override
     public Optional<LocalDateTime> getCreatedDate() {
          return Optional.ofNullable(this.createdDate);
     }

     @Override
     public Optional<String> getCreatedBy() {
          return Optional.ofNullable(this.createdBy);
     }

     @Override
     public Optional<String> getLastModifiedBy() {
          return Optional.ofNullable(this.lastModifiedBy);
     }

     @Override
     public Long getId() {
          return id;
     }

     @Override
     public void setCreatedBy(String createdBy) {
          this.createdBy = createdBy;
     }

     @Override
     public void setCreatedDate(LocalDateTime creationDate) {
          this.createdDate = creationDate;

     }

     @Override
     public void setLastModifiedBy(String lastModifiedBy) {
          this.lastModifiedBy = lastModifiedBy;

     }

     @Override
     public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
          this.lastModifiedDate = lastModifiedDate;

     }

}

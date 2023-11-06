package com.yora.ladder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"key", "section", "address"})
@Table(name = "t_entry", indexes = {
          @Index(name = "key_index", columnList = "entry_key", unique = false),
          @Index(name = "address_step_index", columnList = "entry_address,step_id", unique = true),
          @Index(name = "key_uni_index", columnList = "entry_key, step_id", unique = true),
          @Index(name = "sec_uni_index", columnList = "entry_section, step_id", unique = true)})
public class Entry extends BaseEntity {

     @Column(name = "entry_key")
     private String key;

     @Column(name = "entry_value")
     private String value;

     @Column(name = "entry_section")
     private String section;

     @Column(name = "entry_address")
     private String address;

     @Column(name = "entry_type")
     @Enumerated(EnumType.STRING)
     private EntryType type;

     @ManyToOne(optional = false)
     @JoinColumn(name = "step_id")
     private Step step;
}

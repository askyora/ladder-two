package com.yora.ladder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true, of = {"name", "address"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_step",
          indexes = {
                    @Index(name = "address_index", columnList = "step_address,client_id",
                              unique = true),
                    @Index(name = "name_index", columnList = "step_name,client_id", unique = true)})
public class Step extends BaseEntity {

     @Column(name = "step_name", length = 20)
     private String name;

     @Column(name = "step_description")
     private String description;

     @Column(name = "step_address", nullable = false)
     private String address;

     @ManyToOne(optional = true)
     @JoinColumn(name = "client_id")
     private Client client;

     @Column
     private boolean inheritable;

     @Column
     private boolean overridable;

     @ManyToOne(optional = true)
     @JoinColumn(name = "parent_id")
     private Step parent;

}

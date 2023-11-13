package com.yora.ladder.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "t_client")
@EqualsAndHashCode(callSuper = true, of = {"code"})
public class Client extends BaseEntity implements Serializable{

     private static final long serialVersionUID = 904586354326974629L;

     @Column(name = "client_code", unique = true, length = 12, nullable = false)
     private String code;

     @Column(name = "client_name", length = 50, nullable = false)
     private String name;

     @Column(name = "client_description", length = 255, nullable = true)
     private String description;

}

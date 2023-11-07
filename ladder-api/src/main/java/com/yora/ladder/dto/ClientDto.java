package com.yora.ladder.dto;

import java.util.Objects;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientDto {

     @NotNull
     @NotEmpty
     private String code;

     @NotNull
     @NotEmpty
     private String name;

     private String description;

     public String getCode() {
          return Objects.nonNull(code) ? code.toUpperCase() : code;
     }
}


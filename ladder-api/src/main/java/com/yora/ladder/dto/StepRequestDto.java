package com.yora.ladder.dto;

import java.util.Objects;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StepRequestDto {

     @NotEmpty(message = "Empty name")
     @Pattern(regexp = "^[a-z0-9_-]*$", message = "un-supported characters for step name.")
     private String name;

     private String description;

     @NotEmpty(message = "Empty clientCode")
     private String clientCode;

     private boolean inheritable;

     private boolean overridable;

     @Pattern(regexp = "^[a-z0-9_-]*$", message = "un-supported characters for parentName.")
     private String parentName;

     public String getName() {
          return Objects.nonNull(name) ? name.toLowerCase() : name;
     }

     public String getParentName() {
          return Objects.nonNull(parentName) ? parentName.toLowerCase() : parentName;
     }

     public String getClientCode() {
          return Objects.nonNull(clientCode) ? clientCode.toUpperCase() : clientCode;
     }
}

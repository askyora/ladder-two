package com.yora.ladder.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryDto {

     @NotEmpty(message = "Empty key")
     @Pattern(regexp = "^[a-z0-9_-]*$", message = "un-supported characters for entry key.")
     private String key;

     @NotEmpty(message = "Empty value")
     private String value;

     @NotEmpty(message = "Empty section")
     private String section;

     @NotEmpty(message = "Empty stepAddress")
     private String stepAddress;

     @NotEmpty(message = "Empty type")
     private String type;

}

package com.yora.ladder.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class EntryRequestDto {

     @NotNull(message = "Empty entry")
     private EntryDto entry;

     @NotEmpty(message = "Empty clientCode")
     private String clientCode;

}

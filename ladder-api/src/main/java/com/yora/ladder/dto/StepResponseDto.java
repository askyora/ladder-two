package com.yora.ladder.dto;

import lombok.Data;

@Data
public class StepResponseDto {

     private String name;

     private String address;

     private String description;

     private ClientDto client;

     private boolean inheritable;

     private boolean overridable;

     private StepResponseDto parent;
}

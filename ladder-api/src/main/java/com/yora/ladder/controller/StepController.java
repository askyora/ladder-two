package com.yora.ladder.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yora.ladder.dto.StepRequestDto;
import com.yora.ladder.dto.StepResponseDto;
import com.yora.ladder.service.StepService;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1.0.0/step")
public class StepController {

     private StepService service;

     @Autowired
     public StepController(StepService service) {
          this.service = service;
     }

     @GetMapping
     @RequestMapping(value = {"/{clientCode}/{key}"})
     public Optional<StepResponseDto> getSteps(
               @PathVariable(value = "clientCode", required = true) String clientCode,
               @PathVariable(value = "key", required = true) String key) {
          return this.service.getStep(clientCode, key);
     }


     @PostMapping
     public ResponseEntity<StepResponseDto> createNew(
               @NotNull @RequestBody StepRequestDto requestDto) {
          Optional<StepResponseDto> dto = service.createNew(requestDto);
          return dto.isEmpty() ? ResponseEntity.internalServerError().build()
                    : ResponseEntity.status(HttpStatus.CREATED).body(dto.get());
     }


}

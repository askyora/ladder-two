package com.yora.ladder.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yora.ladder.dto.ClientDto;
import com.yora.ladder.service.ClientService;



@RestController
@RequestMapping("/api/v1.0.0/client")
public class ClientController {

     private ClientService service;

     @Autowired
     public ClientController(ClientService service) {
          this.service = service;
     }

     @PostMapping
     public ResponseEntity<ClientDto> createNew(@RequestBody ClientDto requestDto) {

          Optional<ClientDto> dto = service.createNew(requestDto);
          return dto.isEmpty() ? ResponseEntity.internalServerError().build()
                    : ResponseEntity.status(HttpStatus.CREATED).body(dto.get());
     }

}

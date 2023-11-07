package com.yora.ladder.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yora.ladder.dto.EntryDto;
import com.yora.ladder.dto.EntryRequestDto;
import com.yora.ladder.service.EntryService;

@RestController
@RequestMapping("/api/v1.0.0/entry")
public class EntryController {

     private EntryService service;

     @Autowired
     public EntryController(EntryService service) {
          this.service = service;
     }

     @GetMapping
     @RequestMapping(value = {"/{clientCode}/{key}"})
     public ResponseEntity<List<EntryDto>> getByAddress(
               @RequestHeader(value = "retrieve-all", defaultValue = "false") boolean retrieveAll,
               @PathVariable(value = "clientCode", required = true) String clientCode,
               @PathVariable(value = "key", required = true) String key) {

          List<EntryDto> entryList = this.service.getEntryList(clientCode, key, retrieveAll);
          return entryList.isEmpty() ? ResponseEntity.notFound().build()
                    : ResponseEntity.ok(entryList);
     }


     @PostMapping
     public ResponseEntity<EntryDto> createNew(@RequestBody EntryRequestDto dto) {
          Optional<EntryDto> response = this.service.createNew(dto);
          if (response.isPresent()) {
               return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
          }
          return ResponseEntity.internalServerError().build();
     }


     @PutMapping
     @RequestMapping(value = {"/", "/{new-step}"})
     public ResponseEntity<EntryDto> update(
               @PathVariable("new-step") Optional<String> newStepToAssign,
               @RequestBody EntryRequestDto dto) {
          Optional<EntryDto> response = this.service.update(dto, newStepToAssign);
          if (response.isPresent()) {
               return ResponseEntity.status(HttpStatus.OK).body(response.get());
          }
          return ResponseEntity.internalServerError().build();
     }
}

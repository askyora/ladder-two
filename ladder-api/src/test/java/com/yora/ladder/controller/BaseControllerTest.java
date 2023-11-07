package com.yora.ladder.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import com.yora.ladder.dto.EntryDto;
import com.yora.ladder.dto.EntryRequestDto;
import com.yora.ladder.dto.StepRequestDto;
import com.yora.ladder.entity.Client;
import com.yora.ladder.entity.Entry;
import com.yora.ladder.entity.EntryType;
import com.yora.ladder.entity.Step;
import com.yora.ladder.repository.ClientRepository;
import com.yora.ladder.repository.EntryRepository;
import com.yora.ladder.repository.StepRepository;

public class BaseControllerTest {

     @Autowired
     protected ClientRepository clientRepository;
     @Autowired
     protected StepRepository stepRepository;
     @Autowired
     protected EntryRepository entryRepository;

     protected String getStepsContent(String clientCode, String name, String description,
               String parent) {
          StepRequestDto dto = new StepRequestDto();
          dto.setName(name);
          dto.setDescription(description);
          dto.setInheritable(true);
          dto.setOverridable(false);
          dto.setClientCode(clientCode);
          if (!Objects.isNull(parent)) {
               dto.setParentName(parent);
          }
          return new Gson().toJson(dto);
     }

     protected String getEntryRequestDto(String clientCode, String stepAddress, String key,
               String value, String type, String section) {
          EntryRequestDto dto = new EntryRequestDto();

          dto.setEntry(new EntryDto());
          dto.setClientCode(clientCode);
          dto.getEntry().setKey(key);
          dto.getEntry().setStepAddress(stepAddress);
          dto.getEntry().setSection(section);
          dto.getEntry().setValue(value);
          dto.getEntry().setType(type);
          return new Gson().toJson(dto);
     }

     protected Client newClient(String name, String code, String description) {
          Client client = Client.builder().code(code).name(name).description(description).build();
          return clientRepository.save(client);
     }

     protected Step newStep(Client c, String name, String address, boolean overridable,
               boolean inheritable, String description) {
          return newStep(c, name, address, overridable, inheritable, description, null);
     }

     protected Entry newEntry(String key, String value, String address, String section, Step step,
               String entryType) {
          try {

               return entryRepository.save(Entry.builder().key(key).value(value).address(address)
                         .section(section).step(step).type(EntryType.valueOf(entryType)).build());

          } catch (Exception e) {
               e.printStackTrace();
          }
          return null;
     }

     protected Step newStep(Client c, String name, String address, boolean overridable,
               boolean inheritable, String description, Step parent) {

          Step step = Step.builder().address(address).name(name).overridable(overridable)
                    .inheritable(inheritable).client(c).description(description).parent(parent)
                    .build();

          return stepRepository.save(step);
     }

}

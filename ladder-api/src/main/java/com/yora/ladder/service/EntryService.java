package com.yora.ladder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import com.yora.ladder.config.CommonMapper;
import com.yora.ladder.dto.EntryDto;
import com.yora.ladder.dto.EntryRequestDto;
import com.yora.ladder.entity.Entry;
import com.yora.ladder.entity.EntryType;
import com.yora.ladder.entity.Step;
import com.yora.ladder.repository.EntryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
public class EntryService extends BaseService {


     private EntryRepository entryRepository;
     private CommonMapper mapper;

     @Autowired
     public EntryService(EntryRepository entryRepository) {
          this.entryRepository = entryRepository;
          this.mapper = Mappers.getMapper(CommonMapper.class);
     }

     public List<EntryDto> getEntryList(String clientCode, String key, boolean allLevels) {

          getClientCodeOrThrow(clientCode);

          if (key.lastIndexOf(SPLITTER_CHAR) > -1) {

               String leaf = key.substring(key.lastIndexOf(SPLITTER_CHAR) + 1);
               String branch = key.substring(0, key.lastIndexOf(SPLITTER_CHAR));
               List<String> keys = new ArrayList<>();

               Arrays.stream(branch.split(SPLITTER_REGEX)).reduce("", (partialString, element) -> {
                    keys.add(partialString.isEmpty() ? element.concat(SPLITTER).concat(leaf)
                              : partialString.concat(SPLITTER).concat(element).concat(SPLITTER)
                                        .concat(leaf));
                    return partialString.isEmpty() ? element
                              : partialString.concat(SPLITTER).concat(element);
               });
               Collections.reverse(keys);

               if (allLevels) {

                    return keys.stream().map(k -> {
                         Optional<Entry> entry =
                                   this.entryRepository.findByAdressAndClientCode(k, clientCode);
                         return entry.isPresent() ? entry.get() : null;
                    }).filter(i -> i != null).map(i -> {
                         EntryDto responseDto = this.mapper.entryToEntryDto(i);
                         responseDto.setStepAddress(i.getStep().getAddress());
                         return responseDto;
                    }).collect(Collectors.toList());

               } else {
                    Optional<EntryDto> entryOptional = keys.stream().map(k -> {
                         Optional<Entry> entry =
                                   this.entryRepository.findByAdressAndClientCode(k, clientCode);
                         if (entry.isPresent()) {
                              EntryDto responseDto = this.mapper.entryToEntryDto(entry.get());
                              responseDto.setStepAddress(entry.get().getStep().getAddress());
                              return responseDto;
                         }
                         return null;
                    }).filter(i -> i != null).findFirst();

                    return entryOptional.isPresent() ? List.of(entryOptional.get()) : List.of();
               }
          }
          return Collections.emptyList();
     }

     public Optional<EntryDto> createNew(@NotNull @Valid EntryRequestDto requestDto) {

          getClientCodeOrThrow(requestDto.getClientCode());

          Optional<Step> stepOptional = getStepOrThrow(requestDto.getEntry().getStepAddress(),
                    requestDto.getClientCode());

          String address = stepOptional.get().getAddress().concat(SPLITTER)
                    .concat(requestDto.getEntry().getKey());

          Optional<Entry> optinalEntry = this.entryRepository.findByAdressAndClientCode(address,
                    requestDto.getClientCode());

          if (optinalEntry.isPresent()) {
               throw new HttpClientErrorException(HttpStatus.CONFLICT,
                         String.format("Entry already exist ! address : %s clientId : %s", address,
                                   requestDto.getClientCode()));
          }

          Entry entry = this.mapper.entryDtoToEntry(requestDto.getEntry());
          entry.setStep(stepOptional.get());
          entry.setAddress(stepOptional.get().getAddress().concat(SPLITTER).concat(entry.getKey()));
          EntryDto responseDto = this.mapper.entryToEntryDto(this.entryRepository.save(entry));
          responseDto.setStepAddress(stepOptional.get().getAddress());
          return Optional.ofNullable(responseDto);
     }



     public Optional<EntryDto> update(@NotNull @Valid EntryRequestDto requestDto,
               Optional<String> newStepAddress) {
          getClientCodeOrThrow(requestDto.getClientCode());

          Optional<Step> newStepOptional = Optional.empty();

          if (newStepAddress.isPresent()) {
               newStepOptional = getStepOrThrow(newStepAddress.get(), requestDto.getClientCode());
          }
          Optional<Step> stepOptional = getStepOrThrow(requestDto.getEntry().getStepAddress(),
                    requestDto.getClientCode());

          String address = stepOptional.get().getAddress().concat(SPLITTER)
                    .concat(requestDto.getEntry().getKey());

          Optional<Entry> entryOptional = this.entryRepository.findByAdressAndClientCode(address,
                    requestDto.getClientCode());
          if (!entryOptional.isPresent()) {
               throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
                         String.format("Entry not found ! address : %s clientId : %s", address,
                                   requestDto.getClientCode()));
          }

          Entry entry = entryOptional.get();
          entry.setStep(newStepOptional.isPresent() ? newStepOptional.get() : stepOptional.get());
          entry.setAddress(
                    newStepOptional.isPresent()
                              ? newStepOptional.get().getAddress().concat(SPLITTER)
                                        .concat(requestDto.getEntry().getKey())
                              : address);
          entry.setValue(requestDto.getEntry().getValue());
          entry.setSection(requestDto.getEntry().getSection());
          entry.setType(EntryType.valueOf(requestDto.getEntry().getType()));

          EntryDto responseDto = this.mapper.entryToEntryDto(this.entryRepository.save(entry));
          responseDto.setStepAddress(entry.getStep().getAddress());
          return Optional.ofNullable(responseDto);
     }


}

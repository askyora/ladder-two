package com.yora.ladder.config;

import org.mapstruct.Mapper;
import com.yora.ladder.dto.ClientDto;
import com.yora.ladder.dto.EntryDto;
import com.yora.ladder.dto.StepRequestDto;
import com.yora.ladder.dto.StepResponseDto;
import com.yora.ladder.entity.Client;
import com.yora.ladder.entity.Entry;
import com.yora.ladder.entity.Step;

@Mapper
public interface CommonMapper {

     StepResponseDto stepToResponse(Step step);

     Step requestToStep(StepRequestDto dto);

     ClientDto clientToResponse(Client save);

     Client requestToClient(ClientDto save);

     Entry entryDtoToEntry(EntryDto dto);

     EntryDto entryToEntryDto(Entry entry);

}

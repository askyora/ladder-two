package com.yora.ladder.service;

import java.util.Objects;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import com.yora.ladder.config.CommonMapper;
import com.yora.ladder.dto.StepRequestDto;
import com.yora.ladder.dto.StepResponseDto;
import com.yora.ladder.entity.Client;
import com.yora.ladder.entity.Step;
import com.yora.ladder.repository.StepRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
public class StepService extends BaseService {


     private StepRepository stepDao;
     private CommonMapper mapper;

     @Autowired
     public StepService(StepRepository stepDao) {
          this.stepDao = stepDao;
          this.mapper = Mappers.getMapper(CommonMapper.class);
     }

     public Optional<StepResponseDto> createNew(@NotNull @Valid StepRequestDto request) {

          Optional<Client> clientOptional = getClientCodeOrThrow(request.getClientCode());

          Optional<Step> stepOptional =
                    findStepByAdressAndClientCode(request.getName(), request.getClientCode());

          stepOptional.ifPresent(s -> {
               throw new HttpClientErrorException(HttpStatus.CONFLICT,
                         String.format("Conflict with existing : %s", s.toString()));
          });


          Step step = this.mapper.requestToStep(request);

          if (Objects.nonNull(request.getParentName())
                    && !request.getParentName().trim().isEmpty()) {
               Optional<Step> stepParentOptional =
                         getStepOrThrow(request.getParentName(), request.getClientCode());
               step.setParent(stepParentOptional.get());
          }
          step.setAddress(step.getParent() != null
                    ? step.getParent().getAddress().concat(SPLITTER).concat(request.getName())
                    : request.getName());
          step.setClient(clientOptional.get());

          return Optional.of(mapper.stepToResponse(stepDao.save(step)));
     }

     public Optional<StepResponseDto> getStep(String clientCode, String key) {
          Optional<Step> step= stepDao.findByAddressAndClientCode(key, clientCode);
          if(step.isEmpty()) {
             return Optional.empty();  
          }
          return Optional.of(mapper.stepToResponse(step.get()));
     }

}

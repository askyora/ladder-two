package com.yora.ladder.service;

import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import com.yora.ladder.config.CommonMapper;
import com.yora.ladder.dto.ClientDto;
import com.yora.ladder.entity.Client;
import com.yora.ladder.repository.ClientRepository;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
public class ClientService {

     private ClientRepository clientRepository;

     private CommonMapper mapper;

     @Autowired
     public ClientService(ClientRepository clientRepository) {
          this.clientRepository = clientRepository;
          this.mapper = Mappers.getMapper(CommonMapper.class);
     }

     @Transactional(rollbackFor = RuntimeException.class)
     public Optional<ClientDto> createNew(@NotNull @Validated ClientDto request) {

          Optional<Client> clientOptional = clientRepository.findByCode(request.getCode());

          clientOptional.ifPresent(s -> {
               throw new HttpClientErrorException(HttpStatus.CONFLICT,
                         String.format("Conflict with existing : %s", s.toString()));
          });

          return Optional.of(mapper
                    .clientToResponse(clientRepository.save(mapper.requestToClient(request))));
     }
}

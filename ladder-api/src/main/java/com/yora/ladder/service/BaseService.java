package com.yora.ladder.service;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.yora.ladder.entity.Client;
import com.yora.ladder.entity.Step;
import com.yora.ladder.repository.ClientRepository;
import com.yora.ladder.repository.StepRepository;

@Service
public class BaseService {

     public static final String SPLITTER = ".";
     public static final String SPLITTER_REGEX = "\\.";
     public static final char SPLITTER_CHAR = '.';

     @Autowired
     private ClientRepository clientRepository;
     @Autowired
     private StepRepository stepRepository;

     protected Optional<Client> getClientCodeOrThrow(String clientCode) {
          Optional<Client> clientOptional = clientRepository.findByCode(clientCode);

          clientOptional.orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    String.format("Client code not found ! : %s", clientCode)));

          return clientOptional;
     }

     protected Optional<Step> getStepOrThrow(String stepAddress, String clientCode) {
          Optional<Step> stepOptional = findStepByAdressAndClientCode(stepAddress, clientCode);

          stepOptional.orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    String.format("Step address not found ! : %s", stepAddress)));
          return stepOptional;
     }

     protected Optional<Step> findStepByAdressAndClientCode(String stepAddress, String clientCode) {
          return this.stepRepository.findByAddressAndClientCode(stepAddress, clientCode);
     }
}

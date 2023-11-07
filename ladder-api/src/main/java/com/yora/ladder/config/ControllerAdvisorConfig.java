package com.yora.ladder.config;


import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ControllerAdvice
@Slf4j
public class ControllerAdvisorConfig extends ResponseEntityExceptionHandler {

     @ExceptionHandler(HttpClientErrorException.class)
     public ProblemDetail handleConstraintViolation(HttpClientErrorException ex,
               WebRequest request) {
          return defaultError(ex.getStatusCode(), ex.getStatusText(), ex.getLocalizedMessage());
     }

     @ExceptionHandler(ConstraintViolationException.class)
     public ProblemDetail handleConstraintViolation(ConstraintViolationException ex,
               WebRequest request) {

          List<String> errors = new ArrayList<>();

          for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
               errors.add(String.format("Class : [%s] , Path : [%s] , Message : [%s]",
                         violation.getRootBeanClass().getName(), violation.getPropertyPath(),
                         violation.getMessage()));
          }

          return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.get(0));
     }



     private ProblemDetail defaultError(HttpStatusCode httpStatusCode, String reasonPhrase,
               String localizedMessage) {
          log.error(reasonPhrase + ": " + localizedMessage);
          return ProblemDetail.forStatusAndDetail(httpStatusCode, localizedMessage);
     }
}

package com.yora.ladder.config;

import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableJpaAuditing
public class SecurityAuditorAware implements AuditorAware<String> {

     @Override
     public Optional<String> getCurrentAuditor() {
          if (SecurityContextHolder.getContext().getAuthentication() == null
                    || SecurityContextHolder.getContext().getAuthentication()
                              .getPrincipal() == null) {
               return Optional.of("unauthenticated");
          }
          UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

          return Optional.of(userDetails.getUsername());

     }
}

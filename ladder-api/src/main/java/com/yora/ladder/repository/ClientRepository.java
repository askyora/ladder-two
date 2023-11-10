package com.yora.ladder.repository;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.yora.ladder.entity.Client;
import jakarta.transaction.Transactional;

@Repository
@Transactional(SUPPORTS)
public interface ClientRepository extends CrudRepository<Client, Long> {
     
     
     @Cacheable(value="ClientCache", key="#code")
     Optional<Client> findByCode(String code);

     @Override
     @Transactional(REQUIRED)
     Client save(Client client);
}

package com.yora.ladder.repository;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.yora.ladder.entity.Client;
import jakarta.transaction.Transactional;

@Repository
@Transactional(SUPPORTS)
public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

     Optional<Client> findByCode(String code);

     @Transactional(REQUIRED)
     Client save(Client entity);
}

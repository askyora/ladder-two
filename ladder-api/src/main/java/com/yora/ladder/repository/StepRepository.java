package com.yora.ladder.repository;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.yora.ladder.entity.Step;
import jakarta.transaction.Transactional;

@Repository
@Transactional(SUPPORTS)
public interface StepRepository extends CrudRepository<Step, Long> {

     @Cacheable("StepCache")
     @Query("select s from Step s join s.client c where s.name=:name and c.code=:code")
     Optional<Step> findByNameAndClientCode(@Param("name") String name, @Param("code") String code);

     @Cacheable("StepCache")
     @Query("select s from Step s join s.client c where c.code=:code and s.address=:address")
     Optional<Step> findByAddressAndClientCode(@Param("address") String address,
               @Param("code") String code);
     
     @Cacheable("StepCache")
     @Override
     @Transactional(REQUIRED)
     Step save(Step entity);

}

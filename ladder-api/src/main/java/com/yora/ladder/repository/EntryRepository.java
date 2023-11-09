package com.yora.ladder.repository;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.yora.ladder.entity.Entry;
import jakarta.transaction.Transactional;

@Repository
@Transactional(SUPPORTS)
public interface EntryRepository extends CrudRepository<Entry, Long> {

     @Override
     @Cacheable("EntryCache")
     @Transactional(REQUIRED)
     Entry save(Entry entity);

     @Cacheable("EntryCache")
     @Query("select e from Entry e join e.step s join s.client c where c.code=:code and e.address=:address")
     Optional<Entry> findByAdressAndClientCode(@Param("address") String address,
               @Param("code") String clientCode);

     @Cacheable("EntryCache")
     @Query("select e from Entry e join e.step s join s.client c where c.code=:code and e.section=:section and e.key=:key")
     Optional<Entry> findBySectionAndEntryName(@Param("section") String section,
               @Param("key") String name);
}

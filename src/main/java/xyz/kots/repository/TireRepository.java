package xyz.kots.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.kots.domain.TireEntity;

/**
 * Created by kots on 23.01.2018.
 */

@Repository
public interface TireRepository extends CrudRepository<TireEntity, Long> {
}

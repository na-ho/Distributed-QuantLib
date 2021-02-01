package com.na_ho.DataProvider.repository;

import com.na_ho.DataProvider.model_persistent.EuropeanOptionMCSobolDataParam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EuropeanOptionMCSobolParamRepo extends CrudRepository<EuropeanOptionMCSobolDataParam, String> {
    Optional<EuropeanOptionMCSobolDataParam> findById(Integer idInt);
}

package com.na_ho.DataProvider.repository;

import com.na_ho.DataProvider.model.EuropeanOptionMCSobolData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EuropeanOptionMCSobolResultRepo extends CrudRepository<EuropeanOptionMCSobolData, String> {
}

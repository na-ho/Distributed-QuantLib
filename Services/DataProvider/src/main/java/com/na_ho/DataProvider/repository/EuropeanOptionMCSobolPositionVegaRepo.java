package com.na_ho.DataProvider.repository;

import com.na_ho.DataProvider.model.EuropeanOptionMCSobolPositionVegaData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EuropeanOptionMCSobolPositionVegaRepo extends CrudRepository<EuropeanOptionMCSobolPositionVegaData, String> {
}
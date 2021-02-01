package com.na_ho.DataProvider.repository;

import com.na_ho.DataProvider.model.BondFixedRateData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BondFixedRateResultRepo extends CrudRepository<BondFixedRateData, String> {
}


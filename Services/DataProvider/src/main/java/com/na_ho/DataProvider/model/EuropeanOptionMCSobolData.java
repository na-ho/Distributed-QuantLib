package com.na_ho.DataProvider.model;


import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("EuropeanOptionMCSobolData")
@Data
public class EuropeanOptionMCSobolData {
    private int id;
    private double netPresentValue;
}
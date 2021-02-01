package com.na_ho.DataProvider.model;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("EuropeanOptionMCSobolPositionVegaData")
@Data
public class EuropeanOptionMCSobolPositionVegaData {
    int id;
    int []calculationIDs;
}

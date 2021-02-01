package com.na_ho.DataProvider.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("BondFixedRateData")
@Data
public class BondFixedRateData {

    private int id;

    private double netPresentValue;
    private double cleanPrice;
    private  double dirtyPrice;

    private double accruedCoupon;
    private double previousCoupon;
    private double nextCoupon;

    private double yield;
}

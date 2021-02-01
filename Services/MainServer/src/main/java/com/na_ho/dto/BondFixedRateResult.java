package com.na_ho.dto;

import lombok.Data;

//public record  BondFixedRateResult (double netPresentValue, double cleanPrice, double dirtyPrice
//        , double accruedCoupon, double previousCoupon, double nextCoupon) {}

@Data
public class BondFixedRateResult {
    boolean hasValue;
    int id;
    double netPresentValue;
    double cleanPrice;
    double dirtyPrice;

    double accruedCoupon;
    double previousCoupon;
    double nextCoupon;

    double yield;
}
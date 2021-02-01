package com.na_ho.dto;

import lombok.Data;

@Data
public class EuropeanOptionMCSobolResult {
    boolean hasValue;
    int id;
    double netPresentValue;
}

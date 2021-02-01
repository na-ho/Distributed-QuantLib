package com.na_ho.dto;

import lombok.Data;

@Data
public class EuropeanOptionMCSobol {
    int calculationId;
    int optionType;

    int evaluationDate_year;
    int evaluationDate_month;
    int evaluationDate_day;

    int settlementDate_year;
    int settlementDate_month;
    int settlementDate_day;

    int maturityDate_year;
    int maturityDate_month;
    int maturityDate_day;

    double riskFreeRate;
    double dividendYield;
    double volatility;
    double strike;

    double underlying;

    int timeSteps;
    int nSamples;
}
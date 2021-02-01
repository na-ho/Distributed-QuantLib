package com.na_ho.DataProvider.model_persistent;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "\"EuropeanOptionMCSobolDataParam\"")
@Data
public class EuropeanOptionMCSobolDataParam {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;
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
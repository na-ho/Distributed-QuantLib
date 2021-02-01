package com.na_ho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BondFixedRate {
    int calculationId;
    int settlementDate_year;
    int settlementDate_month;
    int settlementDate_day;

    int fixingDays;
    int settlementDays;

    double zc3mQuote;
    double zc6mQuote;
    double zc1yQuote;

    double redemption;
    int numberOfBonds;

    int[] issueDates_year;
    int[] issueDates_month;
    int[] issueDates_day;
    int[] maturities_year;
    int[] maturities_month;
    int[] maturities_day;

    double[] couponRates;
    double[] marketQuotes;

    int effectiveDate_year;
    int effectiveDate_month;
    int effectiveDate_day;
    int terminationDate_year;
    int terminationDate_month;
    int terminationDate_day;

    double fixedPercentage;
}

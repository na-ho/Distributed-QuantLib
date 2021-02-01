package com.na_ho.pricer.jni;


public class QuantJNI {


    public void loadDLL() {
        System.loadLibrary("QuantJNI");
    }

    public native void sayHello();

    public native double[] priceFixedRateBond(int settlementDate_year, int settlementDate_month, int settlementDate_day
            , int fixingDays, int settlementDays, double zc3mQuote, double zc6mQuote, double zc1yQuote, double redemption
            , int numberOfBonds, int[] issueDates_year, int[] issueDates_month, int[] issueDates_day
            , int[] maturities_year, int[] maturities_month, int[] maturities_day
            , double[] couponRates, double[] marketQuotes
            , int effectiveDate_year, int effectiveDate_month, int effectiveDate_day
            , int terminationDate_year, int terminationDate_month, int terminationDate_day
            , double fixedPercentage);

    public native double[] priceEuropeanOptionMCSobol(int optionType, int settlementDate_year, int settlementDate_month, int settlementDate_day
            , int evaluationDate_year, int evaluationDate_month, int evaluationDate_day
            , int maturityDate_year, int maturityDate_month, int maturityDate_day
            , double riskFreeRate, double dividendYield, double volatility, double strike
            , double underlying
            , int timeSteps
            , int nSamples);
}

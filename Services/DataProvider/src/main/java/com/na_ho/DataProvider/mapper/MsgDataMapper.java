package com.na_ho.DataProvider.mapper;

import com.na_ho.DataProvider.model.BondFixedRateData;
import com.na_ho.DataProvider.model.EuropeanOptionMCSobolData;
import com.na_ho.DataProvider.model.EuropeanOptionMCSobolPositionVegaData;
import com.na_ho.DataProvider.model_persistent.EuropeanOptionMCSobolDataParam;
import com.na_ho.dto.BondFixedRateResult;
import com.na_ho.dto.EuropeanOptionMCSobol;
import com.na_ho.dto.EuropeanOptionMCSobolPositionVega;
import com.na_ho.dto.EuropeanOptionMCSobolResult;

public final class MsgDataMapper {
    public static BondFixedRateData from(BondFixedRateResult msg) {
        BondFixedRateData data = new BondFixedRateData();
        data.setId(msg.getId());
        data.setNetPresentValue(msg.getNetPresentValue());
        data.setCleanPrice(msg.getCleanPrice());
        data.setDirtyPrice(msg.getDirtyPrice());
        data.setAccruedCoupon(msg.getAccruedCoupon());
        data.setPreviousCoupon(msg.getPreviousCoupon());
        data.setNextCoupon(msg.getNextCoupon());
        data.setYield(msg.getYield());
        return data;
    }

    public static BondFixedRateResult from(BondFixedRateData data) {
        BondFixedRateResult result = new BondFixedRateResult();
        result.setId(data.getId());
        result.setHasValue(true);
        result.setNetPresentValue(data.getNetPresentValue());
        result.setCleanPrice(data.getCleanPrice());
        result.setDirtyPrice(data.getDirtyPrice());
        result.setAccruedCoupon(data.getAccruedCoupon());
        result.setPreviousCoupon(data.getPreviousCoupon());
        result.setNextCoupon(data.getNextCoupon());
        result.setYield(data.getYield());
        return result;
    }

    public static EuropeanOptionMCSobolData from(EuropeanOptionMCSobolResult msg) {
        EuropeanOptionMCSobolData data = new EuropeanOptionMCSobolData();
        data.setId(msg.getId());
        data.setNetPresentValue(msg.getNetPresentValue());
        return data;
    }

    public static EuropeanOptionMCSobolResult from(EuropeanOptionMCSobolData data) {
        EuropeanOptionMCSobolResult result = new EuropeanOptionMCSobolResult();
        result.setId(data.getId());
        result.setHasValue(true);
        result.setNetPresentValue(data.getNetPresentValue());
        return result;
    }

    public static EuropeanOptionMCSobolDataParam from(EuropeanOptionMCSobol msg) {
        EuropeanOptionMCSobolDataParam data = new EuropeanOptionMCSobolDataParam();
        data.setOptionType(msg.getOptionType());
        data.setEvaluationDate_year(msg.getEvaluationDate_year());
        data.setEvaluationDate_month(msg.getEvaluationDate_month());
        data.setEvaluationDate_day(msg.getEvaluationDate_day());
        data.setSettlementDate_year(msg.getSettlementDate_year());
        data.setSettlementDate_month(msg.getSettlementDate_month());
        data.setSettlementDate_day(msg.getSettlementDate_day());
        data.setMaturityDate_year(msg.getMaturityDate_year());
        data.setMaturityDate_month(msg.getMaturityDate_month());
        data.setMaturityDate_day(msg.getMaturityDate_day());
        data.setRiskFreeRate(msg.getRiskFreeRate());
        data.setDividendYield(msg.getDividendYield());
        data.setVolatility(msg.getVolatility());
        data.setStrike(msg.getStrike());
        data.setUnderlying(msg.getUnderlying());
        data.setTimeSteps(msg.getTimeSteps());
        data.setNSamples(msg.getNSamples());
        return data;
    }

    public static EuropeanOptionMCSobol from(EuropeanOptionMCSobolDataParam dataOrg) {
        EuropeanOptionMCSobol data = new EuropeanOptionMCSobol();
        data.setOptionType(dataOrg.getOptionType());
        data.setEvaluationDate_year(dataOrg.getEvaluationDate_year());
        data.setEvaluationDate_month(dataOrg.getEvaluationDate_month());
        data.setEvaluationDate_day(dataOrg.getEvaluationDate_day());
        data.setSettlementDate_year(dataOrg.getSettlementDate_year());
        data.setSettlementDate_month(dataOrg.getSettlementDate_month());
        data.setSettlementDate_day(dataOrg.getSettlementDate_day());
        data.setMaturityDate_year(dataOrg.getMaturityDate_year());
        data.setMaturityDate_month(dataOrg.getMaturityDate_month());
        data.setMaturityDate_day(dataOrg.getMaturityDate_day());
        data.setRiskFreeRate(dataOrg.getRiskFreeRate());
        data.setDividendYield(dataOrg.getDividendYield());
        data.setVolatility(dataOrg.getVolatility());
        data.setStrike(dataOrg.getStrike());
        data.setUnderlying(dataOrg.getUnderlying());
        data.setTimeSteps(dataOrg.getTimeSteps());
        data.setNSamples(dataOrg.getNSamples());
        return data;
    }

    public static EuropeanOptionMCSobolPositionVegaData from(EuropeanOptionMCSobolPositionVega dataOrg) {
        EuropeanOptionMCSobolPositionVegaData result = new EuropeanOptionMCSobolPositionVegaData();
        result.setId(dataOrg.getId());
        result.setCalculationIDs(dataOrg.getCalculationIDs());
        return result;
    }

    public static EuropeanOptionMCSobolPositionVega from(EuropeanOptionMCSobolPositionVegaData dataOrg) {
        EuropeanOptionMCSobolPositionVega result = new EuropeanOptionMCSobolPositionVega();
        result.setId(dataOrg.getId());
        result.setCalculationIDs(dataOrg.getCalculationIDs());
        return result;
    }
}

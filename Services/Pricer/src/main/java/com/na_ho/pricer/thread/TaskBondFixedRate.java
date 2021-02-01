package com.na_ho.pricer.thread;

import com.na_ho.dto.BondFixedRate;
import com.na_ho.dto.BondFixedRateResult;
import com.na_ho.util.MsgConst;
import org.springframework.kafka.core.KafkaTemplate;

public class TaskBondFixedRate extends QuantLibRunnable {

    private BondFixedRate bondFixedRate = null;

    private KafkaTemplate<String, Object> template;

    public TaskBondFixedRate(BondFixedRate bondFixedRate, KafkaTemplate<String, Object> template) {
        this.bondFixedRate = bondFixedRate;
        this.template = template;
    }

    @Override
    public void run() {
        int settlementDate_year = bondFixedRate.getSettlementDate_year();
        int settlementDate_month = bondFixedRate.getSettlementDate_month();
        int settlementDate_day = bondFixedRate.getSettlementDate_day();

        int fixingDays = bondFixedRate.getFixingDays();
        int settlementDays = bondFixedRate.getSettlementDays();

        double zc3mQuote = bondFixedRate.getZc3mQuote();
        double zc6mQuote = bondFixedRate.getZc6mQuote();
        double zc1yQuote = bondFixedRate.getZc1yQuote();

        double redemption = bondFixedRate.getRedemption();

        int numberOfBonds = bondFixedRate.getNumberOfBonds();

        int[] issueDates_year = bondFixedRate.getIssueDates_year();
        int[] issueDates_month = bondFixedRate.getIssueDates_month();
        int[] issueDates_day = bondFixedRate.getIssueDates_day();
        int[] maturities_year = bondFixedRate.getMaturities_year();
        int[] maturities_month = bondFixedRate.getMaturities_month();
        int[] maturities_day = bondFixedRate.getMaturities_day();

        double[] couponRates = bondFixedRate.getCouponRates();
        double[] marketQuotes = bondFixedRate.getMarketQuotes();

        int effectiveDate_year = bondFixedRate.getEffectiveDate_year();
        int effectiveDate_month = bondFixedRate.getEffectiveDate_month();
        int effectiveDate_day = bondFixedRate.getEffectiveDate_day();
        int terminationDate_year = bondFixedRate.getTerminationDate_year();
        int terminationDate_month = bondFixedRate.getTerminationDate_month();
        int terminationDate_day = bondFixedRate.getTerminationDate_day();

        double fixedPercentage = bondFixedRate.getFixedPercentage();

        double[] priceResults = quantJNI.priceFixedRateBond(settlementDate_year, settlementDate_month, settlementDate_day, fixingDays, settlementDays, zc3mQuote, zc6mQuote, zc1yQuote, redemption, numberOfBonds, issueDates_year, issueDates_month
                , issueDates_day, maturities_year, maturities_month, maturities_day, couponRates, marketQuotes, effectiveDate_year
                , effectiveDate_month, effectiveDate_day, terminationDate_year, terminationDate_month, terminationDate_day, fixedPercentage);

        //BondFixedRateResult result = new BondFixedRateResult(priceResults[0], priceResults[1], priceResults[2], priceResults[3], priceResults[4], priceResults[5]);
        BondFixedRateResult result = new BondFixedRateResult();
        result.setHasValue(true);
        result.setNetPresentValue(priceResults[0]);
        result.setCleanPrice(priceResults[1]);
        result.setDirtyPrice(priceResults[2]);
        result.setAccruedCoupon(priceResults[3]);
        result.setPreviousCoupon(priceResults[4]);
        result.setNextCoupon(priceResults[5]);
        result.setYield(priceResults[6]);

        result.setId(bondFixedRate.getCalculationId());
        this.template.send(MsgConst.TOPIC_NAME_BOND_FIXED_RATE_RESULT, result);
    }
}

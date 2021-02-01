package com.na_ho.pricer.thread;

import com.na_ho.dto.EuropeanOptionMCSobol;
import com.na_ho.dto.EuropeanOptionMCSobolResult;
import com.na_ho.util.MsgConst;
import org.springframework.kafka.core.KafkaTemplate;

public class TaskEuropeanOptionMCSobol extends QuantLibRunnable {

    private EuropeanOptionMCSobol europeanOptionMCSobol = null;
    private KafkaTemplate<String, Object> template;

    public TaskEuropeanOptionMCSobol(EuropeanOptionMCSobol europeanOptionMCSobol, KafkaTemplate<String, Object> template) {
        this.europeanOptionMCSobol = europeanOptionMCSobol;
        this.template = template;
    }

    @Override
    public void run() {

        double[] priceResults = quantJNI.priceEuropeanOptionMCSobol(europeanOptionMCSobol.getOptionType()
                , europeanOptionMCSobol.getSettlementDate_year()
                , europeanOptionMCSobol.getSettlementDate_month()
                , europeanOptionMCSobol.getSettlementDate_day()
                , europeanOptionMCSobol.getEvaluationDate_year()
                , europeanOptionMCSobol.getEvaluationDate_month()
                , europeanOptionMCSobol.getEvaluationDate_day()
                , europeanOptionMCSobol.getMaturityDate_year()
                , europeanOptionMCSobol.getMaturityDate_month()
                , europeanOptionMCSobol.getMaturityDate_day()
                , europeanOptionMCSobol.getRiskFreeRate()
                , europeanOptionMCSobol.getDividendYield()
                , europeanOptionMCSobol.getVolatility()
                , europeanOptionMCSobol.getStrike()
                , europeanOptionMCSobol.getUnderlying()
                , europeanOptionMCSobol.getTimeSteps()
                , europeanOptionMCSobol.getNSamples());

        EuropeanOptionMCSobolResult result = new EuropeanOptionMCSobolResult();
        result.setHasValue(true);
        result.setId(europeanOptionMCSobol.getCalculationId());
        result.setNetPresentValue(priceResults[0]);
        this.template.send(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_RESULT, result);
    }
}

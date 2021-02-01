package com.na_ho.mainserver.controller;

import com.na_ho.dto.*;
import com.na_ho.util.MsgConst;
import com.na_ho.util.enumType.OptionType;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class MainServerController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Bean
    public NewTopic topicBondFixedRate() {
        return TopicBuilder.name(MsgConst.TOPIC_NAME_BOND_FIXED_RATE)
                .partitions(MsgConst.NUM_PARTITIONS_BOND_FIXED_RATE)
                .replicas((short) 1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic topicEuropeanOptionMCSobol() {
        return TopicBuilder.name(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL)
                .partitions(MsgConst.NUM_PARTITIONS_EUROPEAN_OPTION_MC_SOBOL)
                .replicas((short) 1)
                .compact()
                .build();
    }

    @GetMapping("/api/hello")
    public String hello() throws Exception {

        return "This is a project for load balancing QuantLib with Spring Boot, Kafka and microservice";
    }

    @GetMapping("/api/getBondFixedRateResult")
    public Object getBondFixedRateResult(@RequestParam int id) throws Exception {

        ProducerRecord<String, Object> record = new ProducerRecord<>(MsgConst.TOPIC_NAME_BOND_FIXED_RATE_RESULT_GET, null, "0", (Object) id);
        var response = this.replyingKafkaTemplate.sendAndReceive(record).get();
        var result = (BondFixedRateResult) response.value();
        if (result.isHasValue()) {
            return result;
        } else {
            return "Cannot find data with id:" + Integer.toString(id);
        }
    }

    @GetMapping("/api/getEuropeanOptionMCSobolResult")
    public Object getEuropeanOptionMCSobolResult(@RequestParam int id) throws Exception {

        ProducerRecord<String, Object> record = new ProducerRecord<>(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_GET, null, "0", (Object) id);
        var response = this.replyingKafkaTemplate.sendAndReceive(record).get();
        var result = (EuropeanOptionMCSobolResult) response.value();
        if (result.isHasValue()) {
            return result;
        } else {
            return "Cannot find data with id:" + Integer.toString(id);
        }
    }

    @GetMapping("/api/priceBondFixedRate")
    public Object priceBondFixedRate() throws Exception {

        // for testing
        var calId = sendBondFixedRate();
        return Map.of(
                "status", "Command priceBondFixedRate Sent",
                "Calculation ID", calId
        );
    }

    @GetMapping("/api/priceEuropeanOptionMCSobol")
    public Object priceEuropeanOptionMCSobol() throws Exception {

        // for testing
        var calId = sendEuropeanOptionMCSobol();
        return Map.of(
                "status", "Command priceEuropeanOptionMCSobol Sent",
                "Calculation ID", calId
        );
    }

    @PostMapping(path = "/api/addEuropeanOptionMCSobolParam", consumes = "application/json", produces = "application/json")
    public Object addEuropeanOptionMCSobolParam(
            @RequestBody EuropeanOptionMCSobol europeanOptionMCSobol)
            throws Exception {
        this.kafkaTemplate.send(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_ADD_DATA, "", europeanOptionMCSobol);
        return Map.of(
                "status", "Add EuropeanOptionMCSobolParam"
        );
    }

    @GetMapping("/api/getEuropeanOptionMCSobolParam")
    public Object getEuropeanOptionMCSobolParam(@RequestParam int id) throws Exception {

        ProducerRecord<String, Object> record = new ProducerRecord<>(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_GET_DATA, null, "0", (Object) id);
        var response = this.replyingKafkaTemplate.sendAndReceive(record).get();
        var result = (EuropeanOptionMCSobol) response.value();
        if (result.getCalculationId() != -1) {
            return result;
        } else {
            return "Cannot find data with id:" + Integer.toString(id);
        }
    }

    @GetMapping("/api/calculatePositionEuropeanOptionMCSobolVega")
    public Object calculatePositionVega(@RequestParam int paramID) throws Exception {

        ProducerRecord<String, Object> record = new ProducerRecord<>(MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA, null, "0", paramID);
        int positionId = (int) this.replyingKafkaTemplate.sendAndReceive(record).get().value();
        return Map.of(
                "status", "Command calculatePositionEuropeanOptionMCSobolVega Sent",
                "paramID", paramID,
                "Position ID", positionId
        );
    }

    @GetMapping("/api/getPositionEuropeanOptionMCSobolVega")
    public Object getPositionEuropeanOptionMCSobolVega(@RequestParam int id) throws Exception {

        ProducerRecord<String, Object> record = new ProducerRecord<>(MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA_GET, null, "0", id);
        var response = this.replyingKafkaTemplate.sendAndReceive(record).get();
        var result = (EuropeanOptionMCSobolPositionVega) response.value();
        if (result.getId() != -1) {
            return result;
        } else {
            return "Cannot find data with id:" + Integer.toString(id);
        }
    }

    private int sendBondFixedRate() throws ParseException, ExecutionException, InterruptedException {

        BondFixedRate bondFixedRate = new BondFixedRate();
        bondFixedRate.setSettlementDate_year(2008);
        bondFixedRate.setSettlementDate_month(9);
        bondFixedRate.setSettlementDate_day(18);
        bondFixedRate.setFixingDays(3);
        bondFixedRate.setSettlementDays(3);
        bondFixedRate.setZc3mQuote(0.0096);
        bondFixedRate.setZc6mQuote(0.0145);
        bondFixedRate.setZc1yQuote(0.0194);
        bondFixedRate.setRedemption(100.0);
        bondFixedRate.setNumberOfBonds(5);
        bondFixedRate.setIssueDates_year(new int[]{2005, 2005, 2006, 2002, 1987});
        bondFixedRate.setIssueDates_month(new int[]{3, 6, 6, 11, 5});
        bondFixedRate.setIssueDates_day(new int[]{15, 15, 30, 15, 15});
        bondFixedRate.setMaturities_year(new int[]{2010, 2011, 2013, 2018, 2038});
        bondFixedRate.setMaturities_month(new int[]{8, 8, 8, 8, 5});
        bondFixedRate.setMaturities_day(new int[]{31, 31, 31, 15, 15});

        bondFixedRate.setCouponRates(new double[]{0.02375, 0.04625, 0.03125, 0.04000, 0.04500});
        bondFixedRate.setMarketQuotes(new double[]{100.390625, 106.21875, 100.59375, 101.6875, 102.140625});

        bondFixedRate.setEffectiveDate_year(2007);
        bondFixedRate.setEffectiveDate_month(5);
        bondFixedRate.setEffectiveDate_day(15);

        bondFixedRate.setTerminationDate_year(2017);
        bondFixedRate.setTerminationDate_month(5);
        bondFixedRate.setTerminationDate_day(15);
        bondFixedRate.setFixedPercentage(0.045);

        ProducerRecord<String, Object> record = new ProducerRecord<>(MsgConst.TOPIC_NAME_BOND_FIXED_RATE_DATA_REQUEST_CALCULATION_ID, null, null, (Object) null);
        int calculationId = (int) this.replyingKafkaTemplate.sendAndReceive(record).get().value();

        bondFixedRate.setCalculationId(calculationId);

        this.kafkaTemplate.send(MsgConst.TOPIC_NAME_BOND_FIXED_RATE, "", bondFixedRate);
        return calculationId;
    }

    private int sendEuropeanOptionMCSobol() throws ParseException, ExecutionException, InterruptedException {
        EuropeanOptionMCSobol europeanOptionMCSobol = new EuropeanOptionMCSobol();

        OptionType optionType = OptionType.PUT;
        europeanOptionMCSobol.setOptionType(optionType.getValue());
        europeanOptionMCSobol.setEvaluationDate_year(1998);
        europeanOptionMCSobol.setEvaluationDate_month(5);
        europeanOptionMCSobol.setEvaluationDate_day(15);

        europeanOptionMCSobol.setSettlementDate_year(1998);
        europeanOptionMCSobol.setSettlementDate_month(5);
        europeanOptionMCSobol.setSettlementDate_day(17);

        europeanOptionMCSobol.setMaturityDate_year(1999);
        europeanOptionMCSobol.setMaturityDate_month(5);
        europeanOptionMCSobol.setMaturityDate_day(17);

        europeanOptionMCSobol.setRiskFreeRate(0.06);
        europeanOptionMCSobol.setDividendYield(0.0);
        europeanOptionMCSobol.setVolatility(0.20);
        europeanOptionMCSobol.setStrike(40.0);
        europeanOptionMCSobol.setUnderlying(36.0);
        europeanOptionMCSobol.setTimeSteps(100);
        europeanOptionMCSobol.setNSamples(1048576);

        ProducerRecord<String, Object> record = new ProducerRecord<>(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_REQUEST_CALCULATION_ID, null, "0", null);
        int calculationId = (int) this.replyingKafkaTemplate.sendAndReceive(record).get().value();
        europeanOptionMCSobol.setCalculationId(calculationId);

        this.kafkaTemplate.send(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL, "", europeanOptionMCSobol);
        return calculationId;
    }
}

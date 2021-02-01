package com.na_ho.DataProvider.controller;

import com.na_ho.DataProvider.mapper.MsgDataMapper;
import com.na_ho.DataProvider.model.BondFixedRateData;
import com.na_ho.DataProvider.model.EuropeanOptionMCSobolData;
import com.na_ho.DataProvider.model.EuropeanOptionMCSobolPositionVegaData;
import com.na_ho.DataProvider.repository.BondFixedRateResultRepo;
import com.na_ho.DataProvider.repository.EuropeanOptionMCSobolParamRepo;
import com.na_ho.DataProvider.repository.EuropeanOptionMCSobolPositionVegaRepo;
import com.na_ho.DataProvider.repository.EuropeanOptionMCSobolResultRepo;
import com.na_ho.dto.BondFixedRateResult;
import com.na_ho.dto.EuropeanOptionMCSobol;
import com.na_ho.dto.EuropeanOptionMCSobolPositionVega;
import com.na_ho.dto.EuropeanOptionMCSobolResult;
import com.na_ho.util.MsgConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class DataProviderController {
    private static final Logger logger =
            LoggerFactory.getLogger(DataProviderController.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    BondFixedRateResultRepo bondFixedRateResultRepoRedis;
    @Autowired
    EuropeanOptionMCSobolResultRepo europeanOptionMCSobolResultRepo;

    @Autowired
    EuropeanOptionMCSobolParamRepo europeanOptionMCSobolParamRepo;

    @Autowired
    EuropeanOptionMCSobolPositionVegaRepo europeanOptionMCSobolPositionVegaRepo;

    private int getNextIdKeySet(String key) {
        var valueHead = template.opsForSet().members(key);
        if (valueHead.isEmpty()) {
            return 0;
        } else {
            Set<Integer> ints = valueHead.stream()
                    .map(s -> Integer.parseInt(s))
                    .collect(Collectors.toSet());
            var maxId = Collections.max(ints);
            return Integer.valueOf(maxId) + 1;
        }
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_BOND_FIXED_RATE_RESULT, containerFactory = "kafkaListenerContainerFactory")
    public void listenBondFixedRate(ConsumerRecord<String, BondFixedRateResult> cr,
                                    @Payload BondFixedRateResult payload) {
        var updateData = MsgDataMapper.from(payload);
        bondFixedRateResultRepoRedis.deleteById(Integer.toString(updateData.getId()));
        bondFixedRateResultRepoRedis.save(updateData);
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_RESULT, containerFactory = "kafkaListenerContainerFactory")
    public void listenEuropeanOptionMCSobol(ConsumerRecord<String, EuropeanOptionMCSobolResult> cr,
                                            @Payload EuropeanOptionMCSobolResult payload) {
        var updateData = MsgDataMapper.from(payload);
        europeanOptionMCSobolResultRepo.deleteById(Integer.toString(updateData.getId()));
        europeanOptionMCSobolResultRepo.save(updateData);
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_BOND_FIXED_RATE_DATA_REQUEST_CALCULATION_ID, containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Object listenBondFixedRateData_RequestCalculationID(ConsumerRecord<String, Object> cr) {

        var nextId = getNextIdKeySet("BondFixedRateData");
        BondFixedRateData data = new BondFixedRateData();
        data.setId(nextId);
        bondFixedRateResultRepoRedis.save(data);
        return nextId;
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_REQUEST_CALCULATION_ID, containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Object listenEuropeanOptionMCSobolData_RequestCalculationID(ConsumerRecord<String, Object> cr) {
        var nextId = getNextIdKeySet("EuropeanOptionMCSobolData");
        EuropeanOptionMCSobolData data = new EuropeanOptionMCSobolData();
        data.setId(nextId);
        europeanOptionMCSobolResultRepo.save(data);
        return nextId;
    }


    @KafkaListener(topics = MsgConst.TOPIC_NAME_BOND_FIXED_RATE_RESULT_GET, containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Object listenBondFixedRateResult_get(ConsumerRecord<String, Object> cr) {
        int id = (int) cr.value();
        var dataOptional = bondFixedRateResultRepoRedis.findById(Integer.toString(id));
        BondFixedRateResult result = null;
        if (dataOptional.isPresent()) {
            result = MsgDataMapper.from(dataOptional.get());
        } else {
            result = new BondFixedRateResult();
            result.setHasValue(false);
        }
        return result;
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_GET, containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Object listenEuropeanOptionMCSobol_get(ConsumerRecord<String, Object> cr) {
        int id = (int) cr.value();
        var dataOptional = europeanOptionMCSobolResultRepo.findById(Integer.toString(id));
        EuropeanOptionMCSobolResult result = null;
        if (dataOptional.isPresent()) {
            result = MsgDataMapper.from(dataOptional.get());
        } else {
            result = new EuropeanOptionMCSobolResult();
            result.setHasValue(false);
        }
        return result;
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_ADD_DATA, containerFactory = "kafkaListenerContainerFactory")
    public void listenEuropeanOptionMCSobol_AddData(ConsumerRecord<String, EuropeanOptionMCSobol> cr,
                                                    @Payload EuropeanOptionMCSobol payload) {
        var updateData = MsgDataMapper.from(payload);
        europeanOptionMCSobolParamRepo.save(updateData);
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_GET_DATA, containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Object listenEuropeanOptionMCSobol_GetData(ConsumerRecord<String, Object> cr) {
        int id = (int) cr.value();
        Integer idInt = id;
        var dataOptional = europeanOptionMCSobolParamRepo.findById(idInt);
        EuropeanOptionMCSobol result = null;
        if (dataOptional.isPresent()) {
            result = MsgDataMapper.from(dataOptional.get());
        } else {
            result = new EuropeanOptionMCSobol();
            result.setCalculationId(-1);
        }
        return result;
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA_REQUEST_CALCULATION_ID, containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Object listenPositionCalculateVega_RequestCalculationID(ConsumerRecord<String, Object> cr) {
        var nextId = getNextIdKeySet("EuropeanOptionMCSobolPositionVegaData");
        EuropeanOptionMCSobolPositionVegaData data = new EuropeanOptionMCSobolPositionVegaData();
        data.setId(nextId);
        europeanOptionMCSobolPositionVegaRepo.save(data);
        return nextId;
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA_SAVE, containerFactory = "kafkaListenerContainerFactory")
    public void listenPositionCalculateVega_Save(ConsumerRecord<String, Object> cr, @Payload EuropeanOptionMCSobolPositionVega payload) {
        var updateData = MsgDataMapper.from(payload);
        europeanOptionMCSobolPositionVegaRepo.deleteById(Integer.toString(updateData.getId()));
        europeanOptionMCSobolPositionVegaRepo.save(updateData);
    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA_GET, containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public Object listenPositionCalculateVega_Get(ConsumerRecord<String, Object> cr) {
        int id = (int) cr.value();
        Integer idInt = id;
        var dataOptional = europeanOptionMCSobolPositionVegaRepo.findById(Integer.toString(id));
        EuropeanOptionMCSobolPositionVega result = null;
        if (dataOptional.isPresent()) {
            result = MsgDataMapper.from(dataOptional.get());
        } else {
            result = new EuropeanOptionMCSobolPositionVega();
            result.setId(-1);
        }
        return result;
    }

}

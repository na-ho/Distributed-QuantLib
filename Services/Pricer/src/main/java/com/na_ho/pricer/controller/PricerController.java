package com.na_ho.pricer.controller;

import com.na_ho.dto.BondFixedRate;
import com.na_ho.dto.EuropeanOptionMCSobol;
import com.na_ho.pricer.thread.QuantLibThreadPool;
import com.na_ho.pricer.thread.TaskBondFixedRate;
import com.na_ho.pricer.thread.TaskEuropeanOptionMCSobol;
import com.na_ho.util.MsgConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PricerController {

    private static final Logger logger =
            LoggerFactory.getLogger(PricerController.class);

    @Autowired
    private QuantLibThreadPool quantLibThreadPool;

    @Autowired
    private KafkaTemplate<String, Object> template;

    @KafkaListener(topics = MsgConst.TOPIC_NAME_BOND_FIXED_RATE, containerFactory = "kafkaListenerContainerFactory")
    public void listenBondFixedRate(ConsumerRecord<String, BondFixedRate> cr,
                                    @Payload BondFixedRate payload) {

        TaskBondFixedRate task = new TaskBondFixedRate(payload, template);
        quantLibThreadPool.execute(task);

    }

    @KafkaListener(topics = MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL, containerFactory = "kafkaListenerContainerFactory")
    public void listenEuropeanOptionMCSobol(ConsumerRecord<String, EuropeanOptionMCSobol> cr,
                                            @Payload EuropeanOptionMCSobol payload) throws InterruptedException {

        TaskEuropeanOptionMCSobol task = new TaskEuropeanOptionMCSobol(payload, template);
        quantLibThreadPool.execute(task);
    }


}

package com.na_ho.pricer;

import com.na_ho.util.MsgConst;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class PricerFactory {

    @Autowired
    private KafkaProperties kafkaProperties;

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

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}

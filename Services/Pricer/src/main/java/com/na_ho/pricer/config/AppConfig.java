package com.na_ho.pricer.config;

import com.na_ho.pricer.thread.QuantLibThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class AppConfig {

    @Bean
    public QuantLibThreadPool quantLibThreadPool() {
        final int numThreads = Runtime.getRuntime().availableProcessors();
        return new QuantLibThreadPool(numThreads);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
        return new KafkaTemplate<>(pf);
    }
}

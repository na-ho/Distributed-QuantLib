package com.na_ho.mainserver.config;

import com.na_ho.util.MsgConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfig {

    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate(ProducerFactory<String, Object> pf,
                                                                                ConcurrentKafkaListenerContainerFactory<String, Object> factory) {
        ConcurrentMessageListenerContainer<String, Object> replyContainer = factory.createContainer("main-reply");
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf,
                                                       ConcurrentKafkaListenerContainerFactory<String, Object> factory) {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}

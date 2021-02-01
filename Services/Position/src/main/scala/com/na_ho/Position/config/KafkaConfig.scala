package com.na_ho.Position.config

import com.na_ho.util.MsgConst
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.{KafkaTemplate, ProducerFactory}
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate

@Configuration
class KafkaConfig {
  @Bean def replyingKafkaTemplate(pf: ProducerFactory[String, AnyRef], factory: ConcurrentKafkaListenerContainerFactory[String, AnyRef]): ReplyingKafkaTemplate[String, AnyRef, AnyRef] = {
    val replyContainer = factory.createContainer("position-reply")
    replyContainer.getContainerProperties.setMissingTopicsFatal(false)
    new ReplyingKafkaTemplate[String, AnyRef, AnyRef](pf, replyContainer)
  }

  @Bean def kafkaTemplate(pf: ProducerFactory[String, AnyRef], factory: ConcurrentKafkaListenerContainerFactory[String, AnyRef]): KafkaTemplate[String, AnyRef] = {
    val kafkaTemplate = new KafkaTemplate[String, AnyRef](pf)
    factory.getContainerProperties.setMissingTopicsFatal(false)

    factory.setReplyTemplate(kafkaTemplate)
    kafkaTemplate
  }
}

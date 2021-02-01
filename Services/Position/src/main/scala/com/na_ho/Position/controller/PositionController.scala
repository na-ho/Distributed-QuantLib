package com.na_ho.Position.controller

import com.na_ho.dto.{EuropeanOptionMCSobol, EuropeanOptionMCSobolPositionVega}
import com.na_ho.util.MsgConst
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.{GetMapping, RestController}

@RestController
class  PositionController {

  @Autowired
  val kafkaTemplate : KafkaTemplate[String, AnyRef] = null

  @Autowired
  val replyingKafkaTemplate : ReplyingKafkaTemplate[String, AnyRef, AnyRef] = null

  @GetMapping(Array("/hello"))
  def hello(): String = "hello from Position service (Scala)"

  @KafkaListener(topics = Array(MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA), containerFactory = "kafkaListenerContainerFactory")
  @SendTo
  def listenPosition_Calculate_Vega(cr: ConsumerRecord[String, Object]) : Integer = {

    val id = cr.value.asInstanceOf[Int]
    val record = new ProducerRecord[String, AnyRef](MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_GET_DATA, null, "0", id.asInstanceOf[AnyRef])
    val response = this.replyingKafkaTemplate.sendAndReceive(record).get
    val europeanOption = response.value.asInstanceOf[EuropeanOptionMCSobol]
    val recordReq = new ProducerRecord[String, AnyRef](MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA_REQUEST_CALCULATION_ID, null, "", null)
    val positionId = this.replyingKafkaTemplate.sendAndReceive(recordReq).get.value.asInstanceOf[Int]

    var percent : Double = 1.0;
    var calIDs = new Array[Int](31)
    for( step <- 0 to 30) {
      val record: ProducerRecord[String, AnyRef] = new ProducerRecord[String, AnyRef](MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL_REQUEST_CALCULATION_ID, null, null, null)
      val calculationId: Int = this.replyingKafkaTemplate.sendAndReceive(record).get.value.asInstanceOf[Int]
      var vol = europeanOption.getVolatility();
      vol *= percent
      europeanOption.setCalculationId(calculationId)
      europeanOption.setVolatility(vol)
      calIDs(step) = calculationId
      this.kafkaTemplate.send(MsgConst.TOPIC_NAME_EUROPEAN_OPTION_MC_SOBOL, "", europeanOption)
      percent += 0.01
    }
    val position = new EuropeanOptionMCSobolPositionVega()
    position.setId(positionId)
    position.setCalculationIDs(calIDs)
    this.kafkaTemplate.send(MsgConst.TOPIC_NAME_POSITION_CALCULATE_VEGA_SAVE, "", position)

    positionId
  }

}

package com.pauta.administracao.kafkaproducer.impl

import com.pauta.administracao.kafkaproducer.service.KafkaProducerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class KafkaProducerServiceImpl(

    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${kafka.topic.name}") private val topicName: String

) : KafkaProducerService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendMessage(message: String): Mono<Boolean> {
        return Mono.just(kafkaTemplate.send(topicName, message))
            .flatMap {
                logger.info("Message sent with success")
                Mono.just(true)
            }
            .onErrorResume {
                logger.error("Error on sent message, error=${it.message}")
                Mono.error(IllegalStateException("Error to send message in kafka"))
            }
    }
}

package com.pauta.administracao.kafkaproducer.impl

import com.pauta.administracao.kafkaproducer.service.KafkaProducerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class KafkaProducerServiceImpl(

    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${kafka.topic.name}") private val topicName: String

) : KafkaProducerService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendMessage(message: String): Mono<Boolean> {
        return kafkaTemplate.send(topicName, message)
            .toMono()
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

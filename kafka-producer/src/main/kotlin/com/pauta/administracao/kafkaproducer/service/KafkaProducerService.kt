package com.pauta.administracao.kafkaproducer.service

import reactor.core.publisher.Mono

interface KafkaProducerService {

    fun sendMessage(message: String): Mono<Boolean>
}

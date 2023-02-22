package com.pauta.administracao.kafkaproducer.dto

import java.time.LocalDateTime

data class KafkaDto(
    val nomePauta: String,
    val horaCriacao: LocalDateTime,
    val horaTermino: LocalDateTime,
    val votoSim: Long,
    val votoNao: Long,
    val totalVotos: Long
)

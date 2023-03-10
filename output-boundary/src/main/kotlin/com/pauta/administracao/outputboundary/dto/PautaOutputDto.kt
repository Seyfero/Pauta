package com.pauta.administracao.outputboundary.dto

import java.time.LocalDateTime

data class PautaOutputDto(
    val id: Long?,
    val pautaNome: String,
    val pautaDataCriacao: LocalDateTime,
    val pautaDuracao: Long = 60,
    val pautaProcessada: Boolean? = false
)

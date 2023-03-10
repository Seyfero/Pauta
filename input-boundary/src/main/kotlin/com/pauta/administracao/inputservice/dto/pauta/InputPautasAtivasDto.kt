package com.pauta.administracao.inputservice.dto.pauta

import java.time.LocalDateTime

data class InputPautasAtivasDto(
    val id: Long?,
    val pautaNome: String,
    val pautaUrl: String,
    val pautaDataCriacao: LocalDateTime,
    val pautaDataExpiracao: LocalDateTime,
    val pautaDuracao: Long = 60,
    val pautaProcessada: Boolean? = false
)

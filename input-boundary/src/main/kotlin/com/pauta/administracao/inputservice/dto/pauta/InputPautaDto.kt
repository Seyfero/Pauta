package com.pauta.administracao.inputservice.dto.pauta

import java.time.LocalDateTime

data class InputPautaDto(
    val id: Long?,
    val pautaNome: String,
    val pautaDataCriacao: LocalDateTime = LocalDateTime.now(),
    val pautaDuracao: Long = 60,
    val pautaVotosTotal: Long? = 0
)

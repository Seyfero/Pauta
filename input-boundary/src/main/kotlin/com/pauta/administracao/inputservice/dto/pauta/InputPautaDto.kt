package com.pauta.administracao.inputservice.dto.pauta

import java.time.LocalDateTime

data class InputPautaDto(
    val id: Long?,
    val pautaNome: String,
    val pautaDataCriacao: LocalDateTime,
    val pautaDuracao: Long = 60,
    val pautaVotosTotal: Long? = 0
)

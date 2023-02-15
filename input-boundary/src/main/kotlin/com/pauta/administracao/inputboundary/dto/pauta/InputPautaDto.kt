package com.pauta.administracao.inputboundary.dto.pauta

import java.time.LocalDate

data class InputPautaDto(
    val pautaNome: String,
    val pautaDataCriacao: LocalDate,
    val pautaDuracao: Long = 60,
    val pautaVotosTotal: Long? = 0
)

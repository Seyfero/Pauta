package com.pauta.administracao.outputboundary.dto

import java.time.LocalDate

data class PautaOutputDto(
    val pautaNome: String,
    val pautaDataCriacao: LocalDate,
    val pautaDuracao: Long = 60,
    val pautaVotosTotal: Long? = 0
)
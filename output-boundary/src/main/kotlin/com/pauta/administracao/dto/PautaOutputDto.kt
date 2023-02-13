package com.pauta.administracao.dto

import java.time.LocalDate

data class PautaOutputDto(
    val pautaNome: String,
    val pautaDataCriacao: LocalDate,
    val pautaDuracao: Long = 60,
    val pautaVotosTotal: Long? = 0
)

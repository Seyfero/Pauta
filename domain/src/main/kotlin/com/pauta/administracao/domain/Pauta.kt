package com.pauta.administracao.domain

import java.time.Duration
import java.time.LocalDate

data class Pauta(
    val pautaNome: String,
    val pautaDataCriacao: LocalDate,
    val pautaDuracao: Long = 60,
    val pautaVotosTotal: Long? = 0
)

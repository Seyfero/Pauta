package com.pauta.administracao.domain

import java.time.LocalDateTime

data class PautaDomain(
    val id: Long?,
    val pautaNome: String,
    val pautaDataCriacao: LocalDateTime,
    val pautaDuracao: Long = 60,
    val pautaProcessada: Boolean? = false
)

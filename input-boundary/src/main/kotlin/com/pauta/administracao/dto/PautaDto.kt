package com.pauta.administracao.dto

import java.time.LocalDate

data class PautaDto(
    val pautaName: String,
    val pautaId: Long,
    val createDatePauta: LocalDate
)

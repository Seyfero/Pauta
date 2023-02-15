package com.pauta.administracao.inputboundary.dto.pauta

import java.time.LocalDate

data class InputPautasAtivasDto(
    val nome: String,
    val url: String,
    val dataCriacao: LocalDate,
    val dataExpiracao: LocalDate
)

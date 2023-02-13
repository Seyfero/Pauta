package com.pauta.administracao.domain

import java.time.Duration
import java.time.LocalDate

data class Pauta(
    val namePauta: String,
    val createTime: LocalDate,
    val duration: Long
)

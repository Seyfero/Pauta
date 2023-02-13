package com.pauta.administracao.domain

import java.time.Duration
import java.time.LocalDate

data class Pauta(
    val nomePauta: String,
    val dataCriacao: LocalDate,
    val duracao: Long = 60,
    val totalVotos: Long? = 0
)

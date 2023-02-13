package com.pauta.administracao.dto

import java.time.LocalDate

data class PautaOutputDto(
    val namePauta: String,
    val createTime: LocalDate,
    val duration: Long = 60,
    val totalVotos: Long? = 0
)

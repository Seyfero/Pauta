package com.pauta.administracao.web.config

import java.time.LocalDateTime

data class ApiError(
    val code: String?,
    val message: String? = null,
    val description: String? = null,
    val timesTemp: LocalDateTime
)

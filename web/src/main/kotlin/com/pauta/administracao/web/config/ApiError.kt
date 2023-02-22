package com.pauta.administracao.web.config

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiError(
    val code: String?,
    val message: String? = null,
    val description: String? = null,
    val timesTemp: LocalDateTime
)

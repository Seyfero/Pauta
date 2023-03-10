package com.pauta.administracao.inputservice.converters.pauta

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautasAtivasDto
import java.time.LocalDateTime

fun PautaDomain.toInputAtivos() = InputPautasAtivasDto(
    id,
    pautaNome,
    pautaUrl = "",
    pautaDataCriacao,
    pautaDataExpiracao = LocalDateTime.now(),
    pautaDuracao,
    pautaProcessada
)

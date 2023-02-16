package com.pauta.administracao.input.converters.pauta

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.input.dto.pauta.InputPautasAtivasDto
import java.time.LocalDateTime

fun PautaDomain.toInputAtivos() = InputPautasAtivasDto(
    id,
    pautaNome,
    pautaUrl = "",
    pautaDataCriacao,
    pautaDataExpiracao = LocalDateTime.now(),
    pautaDuracao,
    pautaVotosTotal
)

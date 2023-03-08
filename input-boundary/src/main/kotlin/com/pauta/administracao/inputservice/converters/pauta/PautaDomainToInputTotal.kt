package com.pauta.administracao.inputservice.converters.pauta

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.inputservice.dto.pauta.InputTodasPautasDto
import java.time.LocalDateTime

fun PautaDomain.toInputTotal() = InputTodasPautasDto(
    id,
    pautaNome,
    pautaUrl = "",
    pautaDataCriacao,
    pautaDataExpiracao = pautaDataCriacao.plusSeconds(pautaDuracao),
    pautaDuracao,
    pautaVotosTotal
)

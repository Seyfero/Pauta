package com.pauta.administracao.inputservice.converters.pauta

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import java.time.LocalDateTime

fun InputPautaDto.toDomain() = PautaDomain(
    id,
    pautaNome,
    pautaDataCriacao = LocalDateTime.now(),
    pautaDuracao,
    pautaVotosTotal
)

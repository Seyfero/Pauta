package com.pauta.administracao.input.converters.pauta

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.input.dto.pauta.InputPautaDto

fun InputPautaDto.toDomain() = PautaDomain(
    id,
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)

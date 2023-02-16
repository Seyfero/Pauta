package com.pauta.administracao.outputboundary.converters.pauta

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.outputboundary.dto.PautaOutputDto

fun PautaOutputDto.toDomain() = PautaDomain(
    id,
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)

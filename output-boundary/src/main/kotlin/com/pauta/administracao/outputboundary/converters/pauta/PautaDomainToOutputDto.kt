package com.pauta.administracao.outputboundary.converters.pauta

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.outputboundary.dto.PautaOutputDto

fun PautaDomain.toOutputDto() = PautaOutputDto(
    id,
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaProcessada
)

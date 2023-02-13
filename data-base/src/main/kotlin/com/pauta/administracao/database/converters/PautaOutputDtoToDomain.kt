package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.outputboundary.dto.PautaOutputDto

fun PautaOutputDto.toDomain() = Pauta(
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)
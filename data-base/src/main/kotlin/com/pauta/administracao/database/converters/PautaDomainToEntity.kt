package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import com.pauta.administracao.database.entity.PautaEntity

fun Pauta.toEntity() = PautaEntity(
    id = null,
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)
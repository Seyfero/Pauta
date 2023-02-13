package com.pauta.administracao.converters

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.dto.PautaOutputDto
import com.pauta.administracao.entity.PautaEntity

fun Pauta.toEntity() = PautaEntity(
    id = null,
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)
package com.pauta.administracao.database.converters.pauta

import com.pauta.administracao.database.entity.PautaEntity
import com.pauta.administracao.domain.PautaDomain

fun PautaDomain.toEntity() = PautaEntity(
    id = null,
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)

package com.pauta.administracao.database.converters.pauta

import com.pauta.administracao.database.entity.PautaEntity
import com.pauta.administracao.domain.PautaDomain

fun PautaEntity.toDomain() = PautaDomain(
    id,
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)

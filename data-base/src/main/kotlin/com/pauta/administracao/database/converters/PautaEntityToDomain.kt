package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.database.entity.PautaEntity

fun PautaEntity.toDomain() = Pauta(
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)
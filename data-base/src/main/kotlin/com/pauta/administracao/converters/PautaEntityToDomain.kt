package com.pauta.administracao.converters

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.entity.PautaEntity

fun PautaEntity.toDomain() = Pauta(
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)
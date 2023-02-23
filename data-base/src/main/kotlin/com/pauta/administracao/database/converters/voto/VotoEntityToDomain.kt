package com.pauta.administracao.database.converters.voto

import com.pauta.administracao.database.entity.VotoEntity
import com.pauta.administracao.domain.VotoDomain

fun VotoEntity.toDomain() = VotoDomain(
    id,
    votoEscolha,
    votoUsuarioCpf,
    votoPauta
)

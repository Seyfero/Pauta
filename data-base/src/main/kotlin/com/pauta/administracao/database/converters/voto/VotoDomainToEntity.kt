package com.pauta.administracao.database.converters.voto

import com.pauta.administracao.database.entity.VotoEntity
import com.pauta.administracao.domain.VotoDomain

fun VotoDomain.toEntity() = VotoEntity(
    id = null,
    votoEscolha,
    votoUsuarioDomainCpf.replace("-", "")
        .replace("/", "")
        .replace(".", ""),
    votoPautaDomainId
)

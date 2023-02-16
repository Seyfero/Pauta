package com.pauta.administracao.database.converters.voto

import com.pauta.administracao.database.converters.pauta.toEntity
import com.pauta.administracao.database.converters.usuario.toEntity
import com.pauta.administracao.database.entity.VotoEntity
import com.pauta.administracao.domain.VotoDomain

fun VotoDomain.toEntity() = VotoEntity(
    id = null,
    votoEscolha,
    votoUsuarioDomain.toEntity(),
    votoPautaDomain.toEntity()
)

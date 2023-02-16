package com.pauta.administracao.database.converters.voto

import com.pauta.administracao.database.converters.pauta.toDomain
import com.pauta.administracao.database.converters.usuario.toDomain
import com.pauta.administracao.database.entity.VotoEntity
import com.pauta.administracao.domain.VotoDomain

fun VotoEntity.toDomain() = VotoDomain(
    id,
    votoEscolha,
    votoUsuario.toDomain(),
    votoPauta.toDomain()
)

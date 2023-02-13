package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Voto
import com.pauta.administracao.database.entity.VotoEntity

fun VotoEntity.toDomain() = Voto(
    votoUsuario.toDomain(),
    votoPauta.toDomain()
)
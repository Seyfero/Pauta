package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Voto
import com.pauta.administracao.database.entity.VotoEntity

fun Voto.toEntity() = VotoEntity(
    id = null,
    votoUsuario.toEntity(),
    votoPauta.toEntity()
)
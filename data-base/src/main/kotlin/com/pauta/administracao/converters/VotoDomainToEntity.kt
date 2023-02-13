package com.pauta.administracao.converters

import com.pauta.administracao.domain.Voto
import com.pauta.administracao.entity.VotoEntity

fun Voto.toEntity() = VotoEntity(
    id = null,
    votoUsuario.toEntity(),
    votoPauta.toEntity()
)
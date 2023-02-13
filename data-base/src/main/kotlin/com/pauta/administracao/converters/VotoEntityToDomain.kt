package com.pauta.administracao.converters

import com.pauta.administracao.domain.Voto
import com.pauta.administracao.entity.VotoEntity

fun VotoEntity.toDomain() = Voto(
    votoUsuario.toDomain(),
    votoPauta.toDomain()
)
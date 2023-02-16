package com.pauta.administracao.outputboundary.converters.voto

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.converters.pauta.toDomain
import com.pauta.administracao.outputboundary.converters.usuario.toDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto

fun VotoOutputDto.toDomain() = VotoDomain(
    id,
    votoUsuario.toDomain(),
    votoPauta.toDomain()
)

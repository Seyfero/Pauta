package com.pauta.administracao.outputboundary.converters.voto

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.converters.pauta.toOutput
import com.pauta.administracao.outputboundary.converters.usuario.toOutputDto
import com.pauta.administracao.outputboundary.dto.VotoOutputDto

fun VotoDomain.toOutputDto() = VotoOutputDto(
    id,
    votoUsuarioDomain.toOutputDto(),
    votoPautaDomain.toOutput()
)

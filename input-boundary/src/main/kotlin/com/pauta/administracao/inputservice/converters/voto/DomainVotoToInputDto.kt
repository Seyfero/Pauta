package com.pauta.administracao.inputservice.converters.voto

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.converters.usuario.toIpuntDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoInternalDto

fun VotoDomain.toInputDto() = InputVotoInternalDto(
    id,
    votoEscolha,
    inputVotoPauta = votoPautaDomain.toInputDto(),
    inputVotoUsuario = votoUsuarioDomain.toIpuntDto()
)

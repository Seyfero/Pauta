package com.pauta.administracao.inputservice.converters.voto

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.converters.usuario.toDomain
import com.pauta.administracao.inputservice.dto.voto.InputVotoDto

fun InputVotoDto.toDomain() = VotoDomain(
    id,
    votoEscolha,
    votoPautaDomain = inputVotoPauta.toDomain(),
    votoUsuarioDomain = inputVotoUsuario.toDomain()
)

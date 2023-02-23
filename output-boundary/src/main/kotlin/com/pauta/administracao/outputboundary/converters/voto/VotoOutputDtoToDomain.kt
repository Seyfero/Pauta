package com.pauta.administracao.outputboundary.converters.voto

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto

fun VotoOutputDto.toDomain() = VotoDomain(
    id,
    votoEscolha,
    votoUsuarioCpf,
    votoPautaId
)

package com.pauta.administracao.inputservice.converters.voto

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.inputservice.dto.voto.InputVotoInternalDto

fun InputVotoInternalDto.toDomain() = VotoDomain(
    id,
    votoEscolha,
    inputVotoUsuarioCpf,
    inputVotoPautaId
)

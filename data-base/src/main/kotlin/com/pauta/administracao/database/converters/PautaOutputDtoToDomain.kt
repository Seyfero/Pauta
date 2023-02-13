package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.dto.PautaOutputDto

fun PautaOutputDto.toDomain() = Pauta(
    pautaNome,
    pautaDataCriacao,
    pautaDuracao,
    pautaVotosTotal
)
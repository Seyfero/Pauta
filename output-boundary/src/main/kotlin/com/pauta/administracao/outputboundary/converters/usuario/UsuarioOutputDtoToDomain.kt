package com.pauta.administracao.outputboundary.converters.usuario

import com.pauta.administracao.domain.UsuarioDomain
import com.pauta.administracao.outputboundary.dto.UsuarioOutputDto

fun UsuarioOutputDto.toDomain() = UsuarioDomain(
    id,
    usuarioCpf
)

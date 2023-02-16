package com.pauta.administracao.outputboundary.converters.usuario

import com.pauta.administracao.domain.UsuarioDomain
import com.pauta.administracao.outputboundary.dto.UsuarioOutputDto

fun UsuarioDomain.toOutputDto() = UsuarioOutputDto(
    id,
    usuarioNome,
    usuarioCpf
)

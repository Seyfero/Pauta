package com.pauta.administracao.inputservice.converters.usuario

import com.pauta.administracao.domain.UsuarioDomain
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto

fun UsuarioDomain.toIpuntDto() = InputUsuarioDto(
    id,
    usuarioCpf
)

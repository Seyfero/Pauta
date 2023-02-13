package com.pauta.administracao.converters

import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.dto.UsuarioOutputDto

fun UsuarioOutputDto.toDomain() = Usuario(
    usuarioNome,
    usuarioCpf
)
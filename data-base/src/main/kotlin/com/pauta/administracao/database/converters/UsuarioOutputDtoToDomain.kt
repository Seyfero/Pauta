package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.dto.UsuarioOutputDto

fun UsuarioOutputDto.toDomain() = Usuario(
    usuarioNome,
    usuarioCpf
)
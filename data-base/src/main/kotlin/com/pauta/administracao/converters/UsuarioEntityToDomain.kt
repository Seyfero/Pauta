package com.pauta.administracao.converters

import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.entity.UsuarioEntity

fun UsuarioEntity.toDomain() = Usuario(
    usuarioNome,
    usuarioCpf
)
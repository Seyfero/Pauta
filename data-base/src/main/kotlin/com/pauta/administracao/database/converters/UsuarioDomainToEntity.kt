package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.database.entity.UsuarioEntity

fun Usuario.toEntity() = UsuarioEntity(
    id = null,
    usuarioNome,
    usuarioCpf
)
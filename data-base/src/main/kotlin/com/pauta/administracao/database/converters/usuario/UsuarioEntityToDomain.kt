package com.pauta.administracao.database.converters.usuario

import com.pauta.administracao.database.entity.UsuarioEntity
import com.pauta.administracao.domain.UsuarioDomain

fun UsuarioEntity.toDomain() = UsuarioDomain(
    id,
    usuarioNome,
    usuarioCpf
)

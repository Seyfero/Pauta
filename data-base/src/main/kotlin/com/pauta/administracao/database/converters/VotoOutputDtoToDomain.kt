package com.pauta.administracao.database.converters

import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.domain.Voto
import com.pauta.administracao.dto.UsuarioOutputDto
import com.pauta.administracao.dto.VotoOutputDto

fun VotoOutputDto.toDomain() = Voto(
    votoUsuario.toDomain(),
    votoPauta.toDomain()
)
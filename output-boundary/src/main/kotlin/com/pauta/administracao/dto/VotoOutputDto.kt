package com.pauta.administracao.dto

data class VotoOutputDto(
    val votoUsuario: UsuarioOutputDto,
    val votoPauta: PautaOutputDto
)

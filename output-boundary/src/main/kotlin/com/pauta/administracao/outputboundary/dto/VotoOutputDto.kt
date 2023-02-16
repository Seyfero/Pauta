package com.pauta.administracao.outputboundary.dto

data class VotoOutputDto(
    val id: Long?,
    val votoUsuario: UsuarioOutputDto,
    val votoPauta: PautaOutputDto
)

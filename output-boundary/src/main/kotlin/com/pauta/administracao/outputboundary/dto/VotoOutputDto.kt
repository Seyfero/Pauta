package com.pauta.administracao.outputboundary.dto

data class VotoOutputDto(
    val votoUsuario: UsuarioOutputDto,
    val votoPauta: PautaOutputDto
)

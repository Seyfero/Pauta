package com.pauta.administracao.outputboundary.dto

data class VotoOutputDto(
    val id: Long?,
    val votoEscolha: String,
    val votoUsuarioCpf: String,
    val votoPautaId: Long?
)

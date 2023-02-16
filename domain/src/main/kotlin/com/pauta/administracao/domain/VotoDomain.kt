package com.pauta.administracao.domain

data class VotoDomain(
    val id: Long?,
    val votoEscolha: String,
    val votoUsuarioDomain: UsuarioDomain,
    val votoPautaDomain: PautaDomain
)

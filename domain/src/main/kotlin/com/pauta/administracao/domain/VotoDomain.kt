package com.pauta.administracao.domain

data class VotoDomain(
    val id: Long?,
    val votoEscolha: String,
    val votoUsuarioDomainCpf: String,
    val votoPautaDomainId: Long?
)

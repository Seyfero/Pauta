package com.pauta.administracao.inputservice.dto.voto

data class InputVotoDto(
    val id: Long?,
    val votoEscolha: String,
    val inputVotoPautaNome: String,
    val inputVotoUsuarioCpf: String
)

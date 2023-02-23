package com.pauta.administracao.inputservice.dto.voto

data class InputVotoInternalDto(
    val id: Long?,
    val votoEscolha: String,
    val inputVotoUsuarioCpf: String,
    val inputVotoPautaId: Long?
)

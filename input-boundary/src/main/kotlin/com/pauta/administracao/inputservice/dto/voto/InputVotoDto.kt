package com.pauta.administracao.inputservice.dto.voto

import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto

data class InputVotoDto(
    val id: Long?,
    val votoEscolha: String,
    val inputVotoPauta: InputPautaDto,
    val inputVotoUsuario: InputUsuarioDto
)

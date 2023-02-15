package com.pauta.administracao.inputboundary.dto.voto

import com.pauta.administracao.inputboundary.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputboundary.dto.pauta.InputPautaDto

data class InputVotoDto(
    val votoUsuario: InputUsuarioDto,
    val votoPauta: InputPautaDto
)

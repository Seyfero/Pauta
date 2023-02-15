package com.pauta.administracao.inputboundary.services.usuario

import com.pauta.administracao.inputboundary.dto.usuario.InputUsuarioDto
import reactor.core.publisher.Mono

interface UpdateUsuarioService {

    fun execute(inputUsuarioDto: InputUsuarioDto): Mono<InputUsuarioDto>

}
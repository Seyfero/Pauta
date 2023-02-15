package com.pauta.administracao.inputboundary.services.usuario

import com.pauta.administracao.inputboundary.dto.usuario.InputUsuarioDto
import reactor.core.publisher.Flux

interface ListUsuariosService {

    fun execute(): Flux<InputUsuarioDto>

}
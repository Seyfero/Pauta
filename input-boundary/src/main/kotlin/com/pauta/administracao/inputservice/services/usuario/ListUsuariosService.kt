package com.pauta.administracao.inputservice.services.usuario

import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import reactor.core.publisher.Flux

interface ListUsuariosService {

    fun execute(): Flux<InputUsuarioDto>
}

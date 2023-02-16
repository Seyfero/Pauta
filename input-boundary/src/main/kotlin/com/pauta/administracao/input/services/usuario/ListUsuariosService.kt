package com.pauta.administracao.input.services.usuario

import com.pauta.administracao.input.dto.usuario.InputUsuarioDto
import reactor.core.publisher.Flux

interface ListUsuariosService {

    fun execute(): Flux<InputUsuarioDto>
}

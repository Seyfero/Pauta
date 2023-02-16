package com.pauta.administracao.input.services.usuario

import com.pauta.administracao.input.dto.usuario.InputUsuarioDto
import reactor.core.publisher.Mono

interface UpdateUsuarioService {

    fun execute(inputUsuarioDto: InputUsuarioDto): Mono<InputUsuarioDto>
}

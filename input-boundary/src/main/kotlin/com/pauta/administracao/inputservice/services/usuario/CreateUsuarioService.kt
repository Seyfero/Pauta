package com.pauta.administracao.inputservice.services.usuario

import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import reactor.core.publisher.Mono

interface CreateUsuarioService {

    fun execute(inputUsuarioDto: InputUsuarioDto): Mono<Boolean>
}

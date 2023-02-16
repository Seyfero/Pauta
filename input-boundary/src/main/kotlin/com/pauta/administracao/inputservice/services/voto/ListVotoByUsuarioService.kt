package com.pauta.administracao.inputservice.services.voto

import com.pauta.administracao.inputservice.dto.voto.InputVotoDto
import reactor.core.publisher.Flux

interface ListVotoByUsuarioService {

    fun execute(inputUsuarioCpf: String): Flux<InputVotoDto>
}

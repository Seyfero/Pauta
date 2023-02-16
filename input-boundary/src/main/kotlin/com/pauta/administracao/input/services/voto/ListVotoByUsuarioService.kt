package com.pauta.administracao.input.services.voto

import com.pauta.administracao.input.dto.voto.InputVotoDto
import reactor.core.publisher.Flux

interface ListVotoByUsuarioService {

    fun execute(inputUsuarioCpf: String): Flux<InputVotoDto>
}

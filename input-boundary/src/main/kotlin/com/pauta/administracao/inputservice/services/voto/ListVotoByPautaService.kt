package com.pauta.administracao.inputservice.services.voto

import com.pauta.administracao.inputservice.dto.voto.InputVotoInternalDto
import reactor.core.publisher.Flux

interface ListVotoByPautaService {

    fun execute(inputPautaNome: String): Flux<InputVotoInternalDto>
}

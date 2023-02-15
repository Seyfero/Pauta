package com.pauta.administracao.inputboundary.services.voto

import com.pauta.administracao.inputboundary.dto.voto.InputVotoDto
import reactor.core.publisher.Flux

interface ListVotoByPautaService {

    fun execute(inputPautaNome: String): Flux<InputVotoDto>

}
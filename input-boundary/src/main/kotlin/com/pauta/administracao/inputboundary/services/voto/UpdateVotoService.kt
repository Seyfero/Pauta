package com.pauta.administracao.inputboundary.services.voto

import com.pauta.administracao.inputboundary.dto.voto.InputVotoDto
import reactor.core.publisher.Mono

interface UpdateVotoService {

    fun execute(inputVotoDto: InputVotoDto): Mono<InputVotoDto>

}
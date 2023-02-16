package com.pauta.administracao.inputservice.services.voto

import com.pauta.administracao.inputservice.dto.voto.InputVotoDto
import reactor.core.publisher.Mono

interface UpdateVotoService {

    fun execute(inputVotoDto: InputVotoDto): Mono<InputVotoDto>
}

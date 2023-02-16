package com.pauta.administracao.input.services.voto

import com.pauta.administracao.input.dto.voto.InputVotoDto
import reactor.core.publisher.Mono

interface UpdateVotoService {

    fun execute(inputVotoDto: InputVotoDto): Mono<InputVotoDto>
}

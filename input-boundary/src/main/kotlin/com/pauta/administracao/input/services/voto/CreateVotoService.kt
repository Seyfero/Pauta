package com.pauta.administracao.input.services.voto

import com.pauta.administracao.input.dto.voto.InputVotoDto
import reactor.core.publisher.Mono

interface CreateVotoService {

    fun execute(inputVotoDto: InputVotoDto): Mono<Boolean>
}

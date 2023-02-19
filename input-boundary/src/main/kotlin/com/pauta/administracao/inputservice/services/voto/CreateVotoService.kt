package com.pauta.administracao.inputservice.services.voto

import com.pauta.administracao.inputservice.dto.voto.InputVotoExternalDto
import reactor.core.publisher.Mono

interface CreateVotoService {

    fun execute(inputVotoExternalDto: InputVotoExternalDto): Mono<Boolean>
}

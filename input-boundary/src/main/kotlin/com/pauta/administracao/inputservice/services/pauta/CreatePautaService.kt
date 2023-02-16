package com.pauta.administracao.inputservice.services.pauta

import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import reactor.core.publisher.Mono

interface CreatePautaService {

    fun execute(inputPautaDto: InputPautaDto): Mono<Boolean>
}

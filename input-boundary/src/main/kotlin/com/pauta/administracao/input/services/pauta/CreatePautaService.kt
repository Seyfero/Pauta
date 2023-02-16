package com.pauta.administracao.input.services.pauta

import com.pauta.administracao.input.dto.pauta.InputPautaDto
import reactor.core.publisher.Mono

interface CreatePautaService {

    fun execute(inputPautaDto: InputPautaDto): Mono<Boolean>
}

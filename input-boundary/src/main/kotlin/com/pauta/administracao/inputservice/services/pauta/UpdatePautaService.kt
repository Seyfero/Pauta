package com.pauta.administracao.inputservice.services.pauta

import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import reactor.core.publisher.Mono

interface UpdatePautaService {

    fun execute(inputPautaDto: InputPautaDto): Mono<InputPautaDto>
}

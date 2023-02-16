package com.pauta.administracao.input.services.pauta

import com.pauta.administracao.input.dto.pauta.InputPautaDto
import reactor.core.publisher.Mono

interface UpdatePautaService {

    fun execute(inputPautaDto: InputPautaDto): Mono<InputPautaDto>
}

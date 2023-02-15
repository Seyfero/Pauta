package com.pauta.administracao.inputboundary.services.pauta

import com.pauta.administracao.inputboundary.dto.pauta.InputPautaDto
import reactor.core.publisher.Mono

interface UpdatePautaService {

    fun execute(inputPautaDto: InputPautaDto): Mono<InputPautaDto>

}
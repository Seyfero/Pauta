package com.pauta.administracao.inputboundary.services.pauta

import com.pauta.administracao.inputboundary.dto.pauta.InputPautasAtivasDto
import reactor.core.publisher.Flux

interface ListPautasAtivasService {

    fun execute(): Flux<InputPautasAtivasDto>

}
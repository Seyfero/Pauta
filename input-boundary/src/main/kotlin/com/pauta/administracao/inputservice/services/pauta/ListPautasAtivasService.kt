package com.pauta.administracao.inputservice.services.pauta

import com.pauta.administracao.inputservice.dto.pauta.InputPautasAtivasDto
import reactor.core.publisher.Flux

interface ListPautasAtivasService {

    fun execute(): Flux<InputPautasAtivasDto>
}

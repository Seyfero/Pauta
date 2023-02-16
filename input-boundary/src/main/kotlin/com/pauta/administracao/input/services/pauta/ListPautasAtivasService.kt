package com.pauta.administracao.input.services.pauta

import com.pauta.administracao.input.dto.pauta.InputPautasAtivasDto
import reactor.core.publisher.Flux

interface ListPautasAtivasService {

    fun execute(): Flux<InputPautasAtivasDto>
}

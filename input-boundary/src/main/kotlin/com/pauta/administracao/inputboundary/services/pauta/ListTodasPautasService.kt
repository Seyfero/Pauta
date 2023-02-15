package com.pauta.administracao.inputboundary.services.pauta

import com.pauta.administracao.inputboundary.dto.pauta.InputTodasPautasDto
import reactor.core.publisher.Flux

interface ListTodasPautasService {

    fun execute(): Flux<InputTodasPautasDto>

}
package com.pauta.administracao.inputservice.services.pauta

import com.pauta.administracao.inputservice.dto.pauta.InputTodasPautasDto
import reactor.core.publisher.Flux

interface ListTodasPautasService {

    fun execute(): Flux<InputTodasPautasDto>
}

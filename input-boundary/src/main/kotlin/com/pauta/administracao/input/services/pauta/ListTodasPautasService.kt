package com.pauta.administracao.input.services.pauta

import com.pauta.administracao.input.dto.pauta.InputTodasPautasDto
import reactor.core.publisher.Flux

interface ListTodasPautasService {

    fun execute(): Flux<InputTodasPautasDto>
}

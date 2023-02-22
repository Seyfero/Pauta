package com.pauta.administracao.inputservice.services.pauta

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DeletePautaService {

    fun execute(inputNome: String): Mono<Boolean>

    fun execute(): Flux<Boolean>
}

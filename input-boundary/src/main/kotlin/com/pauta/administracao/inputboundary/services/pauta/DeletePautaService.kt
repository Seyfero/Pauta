package com.pauta.administracao.inputboundary.services.pauta

import reactor.core.publisher.Mono

interface DeletePautaService {

    fun execute(inputId: Long): Mono<Boolean>

    fun execute(inputNome: String): Mono<Boolean>

}
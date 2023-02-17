package com.pauta.administracao.inputservice.services.voto

import reactor.core.publisher.Mono

interface DeleteVotoService {
    fun execute(idVoto: Long): Mono<Boolean>
}

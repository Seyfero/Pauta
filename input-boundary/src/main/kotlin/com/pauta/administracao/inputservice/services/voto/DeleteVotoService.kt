package com.pauta.administracao.inputservice.services.voto

import reactor.core.publisher.Mono

interface DeleteVotoService {
    fun deleteVotoById(idVoto: Long): Mono<Boolean>
}

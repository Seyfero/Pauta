package com.pauta.administracao.inputservice.services.voto

import reactor.core.publisher.Mono

interface ListVotoByEscolhaService {

    fun execute(idVotoPauta: Long, votoEscolha: String): Mono<Long>
}

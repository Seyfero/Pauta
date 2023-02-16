package com.pauta.administracao.inputservice.services.usuario

import reactor.core.publisher.Mono

interface DeleteUsuarioService {

    fun execute(inputUsuarioId: Long): Mono<Boolean>

    fun execute(inputUsuarioCpf: String): Mono<Boolean>
}

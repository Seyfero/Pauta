package com.pauta.administracao.outputboundary.service.gateway

import reactor.core.publisher.Mono

interface ValidateExternalCallUserCpfService {
    fun validateExternalCallUserCpf(cpf: String): Mono<String>
}

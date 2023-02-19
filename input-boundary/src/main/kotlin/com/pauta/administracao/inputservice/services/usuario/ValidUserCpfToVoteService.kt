package com.pauta.administracao.inputservice.services.usuario

import com.pauta.administracao.inputservice.dto.usuario.ExternalVerificationCpfUser
import reactor.core.publisher.Mono

interface ValidUserCpfToVoteService {
    fun execute(cpf: String): Mono<ExternalVerificationCpfUser>
}

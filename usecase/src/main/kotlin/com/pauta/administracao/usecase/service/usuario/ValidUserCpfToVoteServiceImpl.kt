package com.pauta.administracao.usecase.service.usuario

import com.pauta.administracao.domain.exception.IllegalUsuarioException
import com.pauta.administracao.inputservice.dto.usuario.ExternalVerificationCpfUser
import com.pauta.administracao.inputservice.services.usuario.ValidUserCpfToVoteService
import com.pauta.administracao.usecase.service.helper.ValidCpfHelper.Companion.checkCpf
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ValidUserCpfToVoteServiceImpl() : ValidUserCpfToVoteService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(cpf: String): Mono<ExternalVerificationCpfUser> {
        return validVoteOnlyVoteUser(cpf)
            .flatMap {
                if (it) {
                    Mono.just(ExternalVerificationCpfUser("ABLE_TO_VOTE"))
                } else {
                    Mono.just(ExternalVerificationCpfUser("UNABLE_TO_VOTE"))
                }
            }
            .onErrorResume {
                logger.error("Error to validate cpf on external flow!")
                Mono.error(IllegalUsuarioException("Error to validate Cpf!"))
            }
    }

    private fun validVoteOnlyVoteUser(cpf: String): Mono<Boolean> {
        return validCpf(cpf)
            .flatMap {
                if (it) {
                    logger.info("Cpf is valid!")
                    Mono.just(true)
                } else {
                    logger.info("Cpf is not valid!")
                    Mono.just(false)
                }
            }
            .onErrorResume {
                logger.error("Occurred error in validation cpf!")
                Mono.error(IllegalAccessException("Error to validate cpf!"))
            }
    }

    private fun validCpf(cpf: String): Mono<Boolean> {
        return checkCpf(cpf)
            .doOnSuccess {
                logger.info("Success to validate cpf!")
            }
            .onErrorResume {
                logger.error("Error to validate cpf!")
                Mono.error(IllegalAccessException("Invalid CPF: ${it.message}"))
            }
    }
}

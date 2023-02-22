package com.pauta.administracao.usecase.service.usuario

import com.pauta.administracao.inputservice.services.usuario.DeleteUsuarioService
import com.pauta.administracao.outputboundary.service.repository.UsuarioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeleteUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : DeleteUsuarioService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputUsuarioId: Long): Mono<Boolean> {
        return verifyIfExistsUsuarioById(inputUsuarioId)
            .flatMap { exists ->
                if (exists) {
                    usuarioService.deleteById(inputUsuarioId).subscribe()
                    logger.info("User deleted with success!")
                    Mono.just(true)
                } else {
                    Mono.error(NoSuchElementException("User not voted!"))
                }
            }
            .onErrorMap { IllegalStateException("Error to verify if user voted!", it) }
    }

    override fun execute(inputUsuarioCpf: String): Mono<Boolean> {
        return verifyIfExistsUsuarioByCpf(inputUsuarioCpf)
            .flatMap { exists ->
                if (exists) {
                    usuarioService.deleteByCpf(inputUsuarioCpf)
                        .map {
                            logger.info("User removed!")
                            true
                        }
                        .onErrorResume { e ->
                            logger.error("Error removing order: ${e.message}")
                            Mono.just(false)
                        }
                } else {
                    Mono.error(NoSuchElementException("User not voted!"))
                }
            }
            .onErrorMap {
                logger.error("Error to verify if user voted!")
                IllegalStateException("Error to verify if user voted!", it)
            }
    }

    private fun verifyIfExistsUsuarioByCpf(cpf: String): Mono<Boolean> {
        return usuarioService.findByCpf(cpf)
            .hasElement()
    }

    private fun verifyIfExistsUsuarioById(id: Long): Mono<Boolean> {
        return usuarioService.findById(id)
            .hasElement()
    }
}

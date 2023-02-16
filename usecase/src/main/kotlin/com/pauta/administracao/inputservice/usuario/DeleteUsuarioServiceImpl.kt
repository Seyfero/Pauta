package com.pauta.administracao.inputservice.usuario

import com.pauta.administracao.inputservice.services.usuario.DeleteUsuarioService
import com.pauta.administracao.outputboundary.service.UsuarioService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeleteUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : DeleteUsuarioService {

    override fun execute(inputUsuarioId: Long): Mono<Boolean> {
        return verifyIfExistsUsuarioById(inputUsuarioId)
            .flatMap { exists ->
                if (exists) {
                    usuarioService.deleteById(inputUsuarioId)
                    Mono.just(true)
                } else {
                    Mono.error(NoSuchElementException("Usuário não encontrado"))
                }
            }
            .onErrorMap { IllegalStateException("Erro ao executar o método execute", it) }
    }

    override fun execute(inputUsuarioCpf: String): Mono<Boolean> {
        return verifyIfExistsUsuarioByCpf(inputUsuarioCpf)
            .flatMap { exists ->
                if (exists) {
                    usuarioService.deleteByCpf(inputUsuarioCpf)
                    Mono.just(true)
                } else {
                    Mono.error(NoSuchElementException("Usuário não encontrado"))
                }
            }
            .onErrorMap { IllegalStateException("Erro ao executar o método execute", it) }
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

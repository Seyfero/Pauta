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
        return try {
            verifyIfExistsUsuarioById(inputUsuarioId).map {
                usuarioService.deleteById(inputUsuarioId)
                return@map true
            }.switchIfEmpty(Mono.error(Exception("Algo")))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    override fun execute(inputUsuarioCpf: String): Mono<Boolean> {
        return try {
            verifyIfExistsUsuarioByCpf(inputUsuarioCpf).map {
                usuarioService.deleteByCpf(inputUsuarioCpf)
                return@map true
            }.switchIfEmpty(Mono.error(Exception("Algo")))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    private fun verifyIfExistsUsuarioByCpf(cpf: String): Mono<Boolean> {
        return usuarioService.findByCpf(cpf)
            .hasElement()
            .switchIfEmpty(Mono.empty())
    }

    private fun verifyIfExistsUsuarioById(id: Long): Mono<Boolean> {
        return usuarioService.findById(id)
            .hasElement()
            .switchIfEmpty(Mono.empty())
    }
}

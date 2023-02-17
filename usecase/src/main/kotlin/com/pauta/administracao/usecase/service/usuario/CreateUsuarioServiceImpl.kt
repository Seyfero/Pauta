package com.pauta.administracao.usecase.service.usuario

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.converters.usuario.toDomain
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.CreateUsuarioService
import com.pauta.administracao.outputboundary.converters.usuario.toOutputDto
import com.pauta.administracao.outputboundary.service.UsuarioService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : CreateUsuarioService {

    override fun execute(inputUsuarioDto: InputUsuarioDto): Mono<Boolean> {
        return verifyIfExistsUsuario(inputUsuarioDto)
            .flatMap { usuarioExists ->
                if (usuarioExists) {
                    Mono.error(IllegalStateException("O usuário já existe"))
                } else {
                    usuarioService.create(inputUsuarioDto.toDomain().toOutputDto())
                    Mono.just(true)
                }
            }
            .onErrorMap { IllegalStateException("Erro ao executar o método execute", it) }
    }

    private fun verifyIfExistsUsuario(inputUsuarioDto: InputUsuarioDto): Mono<Boolean> {
        return usuarioService.findByCpf(inputUsuarioDto.usuarioCpf)
            .hasElement()
    }
}

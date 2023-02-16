package com.pauta.administracao.inputservice.usuario

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
        return try {
            verifyIfExistsUsuario(inputUsuarioDto).map {
                usuarioService.create(inputUsuarioDto.toDomain().toOutputDto())
                return@map true
            }.switchIfEmpty(Mono.error(Exception("Algo")))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    private fun verifyIfExistsUsuario(inputUsuarioDto: InputUsuarioDto): Mono<Boolean> {
        return usuarioService.findByCpf(inputUsuarioDto.usuarioCpf)
            .hasElement()
            .switchIfEmpty(Mono.empty())
    }
}

package com.pauta.administracao.inputservice.usuario

import com.pauta.administracao.inputservice.converters.usuario.toIpuntDto
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.ListUsuariosService
import com.pauta.administracao.outputboundary.service.UsuarioService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ListUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : ListUsuariosService {

    override fun execute(): Flux<InputUsuarioDto> {
        return usuarioService.findAll()
            .flatMap {
                Flux.fromIterable(listOf(it.toIpuntDto()))
            }
            .onErrorResume { throwable: Throwable ->
                Flux.error(IllegalStateException("Erro ao executar o método execute", throwable))
            }
    }

}

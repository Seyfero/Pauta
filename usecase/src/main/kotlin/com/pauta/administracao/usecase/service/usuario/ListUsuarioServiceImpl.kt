package com.pauta.administracao.usecase.service.usuario

import com.pauta.administracao.inputservice.converters.usuario.toIpuntDto
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.ListUsuariosService
import com.pauta.administracao.outputboundary.service.UsuarioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ListUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : ListUsuariosService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(): Flux<InputUsuarioDto> {
        return usuarioService.findAll()
            .flatMap {
                Flux.fromIterable(listOf(it.toIpuntDto()))
            }
            .doOnTerminate {
                logger.info("User founded with success!")
            }
            .onErrorResume { throwable: Throwable ->
                logger.error("Error to founded user!")
                Flux.error(IllegalStateException("Error to retrieve users!", throwable))
            }
    }
}

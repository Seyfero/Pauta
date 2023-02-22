package com.pauta.administracao.usecase.service.usuario

import com.pauta.administracao.inputservice.converters.usuario.toDomain
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.CreateUsuarioService
import com.pauta.administracao.outputboundary.converters.usuario.toOutputDto
import com.pauta.administracao.outputboundary.service.repository.UsuarioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : CreateUsuarioService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputUsuarioDto: InputUsuarioDto): Mono<Boolean> {
        return verifyIfExistsUsuario(inputUsuarioDto)
            .flatMap { usuarioExists ->
                if (usuarioExists) {
                    logger.error("This user voted before on this order!")
                    Mono.error(IllegalStateException("This user voted before on this order!"))
                } else {
                    usuarioService.create(inputUsuarioDto.toDomain().toOutputDto()).subscribe()
                    logger.info("Used created!")
                    Mono.just(true)
                }
            }
            .onErrorMap { IllegalStateException("Error to found user", it) }
    }

    private fun verifyIfExistsUsuario(inputUsuarioDto: InputUsuarioDto): Mono<Boolean> {
        return usuarioService.findByCpf(inputUsuarioDto.usuarioCpf)
            .hasElement()
    }
}

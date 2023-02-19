package com.pauta.administracao.usecase.service.usuario

import com.pauta.administracao.inputservice.converters.usuario.toDomain
import com.pauta.administracao.inputservice.converters.usuario.toIpuntDto
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.UpdateUsuarioService
import com.pauta.administracao.outputboundary.converters.usuario.toOutputDto
import com.pauta.administracao.outputboundary.service.repository.UsuarioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdateUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : UpdateUsuarioService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputUsuarioDto: InputUsuarioDto): Mono<InputUsuarioDto> {
        return usuarioService.findByCpf(inputUsuarioDto.usuarioCpf)
            .flatMap { usuario ->
                usuarioService.update(inputUsuarioDto.toDomain().toOutputDto().copy(id = usuario.id))
                    .map { it.toIpuntDto() }
                    .doOnSuccess {
                        logger.info("User founded with success!")
                    }
            }
            .switchIfEmpty(Mono.error(NoSuchElementException("User not found!")))
            .onErrorMap { throwable: Throwable ->
                logger.error("Error to update user!")
                IllegalStateException("Error to find user by cpf!", throwable)
            }
    }
}

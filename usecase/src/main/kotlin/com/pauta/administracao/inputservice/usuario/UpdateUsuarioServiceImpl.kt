package com.pauta.administracao.inputservice.usuario

import com.pauta.administracao.inputservice.converters.usuario.toDomain
import com.pauta.administracao.inputservice.converters.usuario.toIpuntDto
import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.UpdateUsuarioService
import com.pauta.administracao.outputboundary.converters.usuario.toOutputDto
import com.pauta.administracao.outputboundary.service.UsuarioService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdateUsuarioServiceImpl(

    private val usuarioService: UsuarioService

) : UpdateUsuarioService {
    override fun execute(inputUsuarioDto: InputUsuarioDto): Mono<InputUsuarioDto> {
        return try {
            usuarioService.findByCpf(inputUsuarioDto.usuarioCpf)
                .flatMap {
                    usuarioService.update(inputUsuarioDto.toDomain().toOutputDto().copy(id = it.id)).map { usuario ->
                        usuario.toIpuntDto()
                    }
                }
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }
}

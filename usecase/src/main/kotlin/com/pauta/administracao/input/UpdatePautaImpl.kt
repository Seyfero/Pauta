package com.pauta.administracao.input

import com.pauta.administracao.input.converters.pauta.toDomain
import com.pauta.administracao.input.converters.pauta.toInputDto
import com.pauta.administracao.input.dto.pauta.InputPautaDto
import com.pauta.administracao.input.services.pauta.UpdatePautaService
import com.pauta.administracao.outputboundary.converters.pauta.toOutput
import com.pauta.administracao.outputboundary.service.PautaService
import reactor.core.publisher.Mono

class UpdatePautaImpl(

    private val pautaService: PautaService

) : UpdatePautaService {

    override fun execute(inputPautaDto: InputPautaDto): Mono<InputPautaDto> {
        return try {
            pautaService.findByName(inputPautaDto.pautaNome)
                .flatMap {
                    pautaService.update(inputPautaDto.toDomain().toOutput().copy(id = it.id)).map { pauta ->
                        pauta.toInputDto()
                    }
                }
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }
}

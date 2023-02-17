package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.services.pauta.UpdatePautaService
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.PautaService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdatePautaServiceImpl(

    private val pautaService: PautaService

) : UpdatePautaService {

    override fun execute(inputPautaDto: InputPautaDto): Mono<InputPautaDto> {
        return pautaService.findByName(inputPautaDto.pautaNome)
            .switchIfEmpty(Mono.error(NoSuchElementException("A pauta não foi encontrada")))
            .flatMap { existingPauta ->
                pautaService.update(inputPautaDto.toDomain().toOutputDto().copy(id = existingPauta.id))
                    .map { updatedPauta -> updatedPauta.toInputDto() }
            }
            .onErrorMap { IllegalStateException("Erro ao executar o método execute", it) }
    }
}

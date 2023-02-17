package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.services.pauta.CreatePautaService
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.PautaService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreatePautaServiceImpl(

    private val pautaService: PautaService

) : CreatePautaService {

    override fun execute(inputPautaDto: InputPautaDto): Mono<Boolean> {
        return verifyIfExistsPauta(inputPautaDto)
            .flatMap { exists ->
                if (exists) {
                    Mono.error(IllegalArgumentException("A pauta já existe"))
                } else {
                    pautaService.create(inputPautaDto.toDomain().toOutputDto())
                    Mono.just(true)
                }
            }
    }

    private fun verifyIfExistsPauta(inputPautaDto: InputPautaDto): Mono<Boolean> {
        return pautaService.findByName(inputPautaDto.pautaNome)
            .hasElement()
    }
}

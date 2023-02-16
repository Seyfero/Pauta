package com.pauta.administracao.inputservice.pauta

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
        return try {
            verifyIfExistsPauta(inputPautaDto).map {
                pautaService.create(inputPautaDto.toDomain().toOutputDto())
                return@map true
            }.switchIfEmpty(Mono.error(Exception("Algo")))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    private fun verifyIfExistsPauta(inputPautaDto: InputPautaDto): Mono<Boolean> {
        return pautaService.findByName(inputPautaDto.pautaNome)
            .hasElement()
            .switchIfEmpty(Mono.empty())
    }
}

package com.pauta.administracao.input

import com.pauta.administracao.input.converters.pauta.toDomain
import com.pauta.administracao.input.dto.pauta.InputPautaDto
import com.pauta.administracao.input.services.pauta.CreatePautaService
import com.pauta.administracao.outputboundary.converters.pauta.toOutput
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
                pautaService.create(inputPautaDto.toDomain().toOutput())
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

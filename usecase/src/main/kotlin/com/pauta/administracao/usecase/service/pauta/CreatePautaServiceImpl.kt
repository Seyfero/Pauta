package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.services.pauta.CreatePautaService
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreatePautaServiceImpl(

    private val pautaService: PautaService

) : CreatePautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputPautaDto: InputPautaDto): Mono<Boolean> {
        return verifyIfExistsPauta(inputPautaDto)
            .flatMap { exists ->
                if (exists) {
                    logger.error("This order exists!")
                    Mono.error(IllegalArgumentException("This order exists!"))
                } else {
                    pautaService.create(inputPautaDto.toDomain().toOutputDto())
                    logger.info("Order created!")
                    Mono.just(true)
                }
            }
    }

    private fun verifyIfExistsPauta(inputPautaDto: InputPautaDto): Mono<Boolean> {
        return pautaService.findByName(inputPautaDto.pautaNome)
            .hasElement()
    }
}

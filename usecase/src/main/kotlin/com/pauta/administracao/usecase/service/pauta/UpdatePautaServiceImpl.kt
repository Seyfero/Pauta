package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.services.pauta.UpdatePautaService
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdatePautaServiceImpl(

    private val pautaService: PautaService

) : UpdatePautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputPautaDto: InputPautaDto): Mono<InputPautaDto> {
        return pautaService.findByName(inputPautaDto.pautaNome)
            .switchIfEmpty(Mono.error(NoSuchElementException("Not found order!")))
            .flatMap { existingPauta ->
                pautaService.update(inputPautaDto.toDomain().toOutputDto().copy(id = existingPauta.id))
                    .map { updatedPauta -> updatedPauta.toInputDto() }
            }
            .doOnSuccess {
                logger.info("Order updated with success!")
            }
            .onErrorMap {
                logger.error("Error to update order!")
                IllegalStateException("server.error.Error to update order!", it)
            }
    }
}

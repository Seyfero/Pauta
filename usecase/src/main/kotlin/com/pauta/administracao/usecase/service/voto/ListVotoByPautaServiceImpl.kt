package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.converters.voto.toInputDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoInternalDto
import com.pauta.administracao.inputservice.services.voto.ListVotoByPautaService
import com.pauta.administracao.outputboundary.service.repository.PautaService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ListVotoByPautaServiceImpl(

    private val votoService: VotoService,
    private val pautaService: PautaService

) : ListVotoByPautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputPautaNome: String): Flux<InputVotoInternalDto> {
        return verifyIfExistsPauta(inputPautaNome)
            .flatMapMany { inputVotoDto ->
                inputVotoDto.id?.let { id ->
                    votoService.findByVotoPautaNome(id)
                        .map {
                            it.toInputDto()
                        }
                        .doOnComplete {
                            logger.info("List of votes founded with success!")
                        }
                }
            }.switchIfEmpty(Flux.empty())
    }

    private fun verifyIfExistsPauta(inputPautaNome: String): Mono<InputPautaDto> {
        return pautaService.findByName(inputPautaNome)
            .map {
                it.toInputDto()
            }
            .doOnSuccess {
                logger.info("Get order by name!")
            }
            .switchIfEmpty(
                Mono.defer {
                    logger.error("Order not found!")
                    Mono.error(NoSuchElementException("Order not found!"))
                }
            )
    }
}

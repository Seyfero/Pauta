package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toInputAtivos
import com.pauta.administracao.inputservice.dto.pauta.InputPautasAtivasDto
import com.pauta.administracao.inputservice.services.pauta.ListPautasAtivasService
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Service
class ListPautasAtivasServiceImpl(

    private val pautaService: PautaService

) : ListPautasAtivasService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(): Flux<InputPautasAtivasDto> {
        return pautaService.findAll()
            .collectList()
            .map { it.filter { pauta -> isValidPautaByDuration(pauta.toInputAtivos()) } }
            .flatMapMany { Flux.fromIterable(it) }
            .map { it.toInputAtivos() }
            .doOnTerminate {
                logger.info("Order founded with success!")
            }
            .onErrorMap {
                logger.error("Error to update order!")
                IllegalStateException("Error on convert active orders!", it)
            }
    }

    private fun isValidPautaByDuration(inputPautasAtivasDto: InputPautasAtivasDto): Boolean {
        return inputPautasAtivasDto.pautaDataCriacao.plusSeconds(inputPautasAtivasDto.pautaDuracao)
            .isBefore(LocalDateTime.now())
    }
}

package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toInputTotal
import com.pauta.administracao.inputservice.dto.pauta.InputTodasPautasDto
import com.pauta.administracao.inputservice.services.pauta.ListTodasPautasService
import com.pauta.administracao.outputboundary.service.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ListTodasPautasServiceImpl(

    private val pautaService: PautaService

) : ListTodasPautasService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(): Flux<InputTodasPautasDto> {
        return pautaService.findAll()
            .map { it.toInputTotal() }
            .doOnTerminate {
                logger.info("Order founded with success!")
            }
            .onErrorMap {
                logger.error("Error to convert order!")
                IllegalStateException("Error on convert all orders!", it)
            }
    }
}

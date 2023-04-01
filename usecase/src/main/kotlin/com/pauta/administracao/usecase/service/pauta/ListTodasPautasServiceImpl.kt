package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toInputTotal
import com.pauta.administracao.inputservice.dto.pauta.InputTodasPautasDto
import com.pauta.administracao.inputservice.services.pauta.ListTodasPautasService
import com.pauta.administracao.outputboundary.service.repository.PautaService
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
                logger.info("All search finalized with success!")
            }
            .onErrorResume { error ->
                logger.error("Error to convert order!")

                Flux.error(
                    IllegalStateException(
                            error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error on convert all orders!" else it
                        }
                    )
                )
            }
    }
}

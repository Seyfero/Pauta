package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.inputservice.services.voto.ListVotoByEscolhaService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ListVotoByEscolhaServiceImpl(

    private val votoService: VotoService

) : ListVotoByEscolhaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(idVotoPauta: Long, votoEscolha: String): Mono<Long> {
        return votoService.getCountVotosByPautaId(idVotoPauta, votoEscolha)
            .doOnSuccess {
                logger.info("Success to get count of votes!")
            }
            .onErrorResume {
                logger.error("Error to count votes message = ${it.message}")
                Mono.error(IllegalAccessException("Error to count votes!"))
            }
    }
}

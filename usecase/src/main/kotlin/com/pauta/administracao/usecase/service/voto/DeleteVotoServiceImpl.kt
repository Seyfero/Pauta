package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.inputservice.services.voto.DeleteVotoService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeleteVotoServiceImpl(

    private val votoService: VotoService,

) : DeleteVotoService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(idVoto: Long): Mono<Boolean> {
        return verifyIfVotoById(idVoto)
            .flatMap {
                logger.info("Vote deleted with success!")
                votoService.delete(idVoto)
            }
            .onErrorResume {
                logger.error("Error to delete vote message = ${it.message}")
                Mono.error(IllegalAccessException("Error te delete vote!"))
            }
    }

    private fun verifyIfVotoById(idVoto: Long?): Mono<Boolean> {
        return votoService.findByVotoPauta(idVoto)
            .hasElements()
    }
}

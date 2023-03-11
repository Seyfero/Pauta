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
                    .map {
                        logger.info("Vote deleted!")
                        true
                    }
                    .onErrorResume { e ->
                        logger.error("Error deleting order: ${e.message}")
                        Mono.just(false)
                    }
            }
            .onErrorResume { error ->

                logger.error("Error to delete vote message = ${error.message}")
                Mono.error(
                    IllegalAccessException(
                        error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error te delete vote!" else it
                        }
                    )
                )
            }
    }

    private fun verifyIfVotoById(idVoto: Long?): Mono<Boolean> {
        return votoService.findByVotoPauta(idVoto)
            .hasElements()
    }
}

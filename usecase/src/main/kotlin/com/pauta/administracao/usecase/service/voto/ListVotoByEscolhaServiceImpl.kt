package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.inputservice.services.voto.ListVotoByEscolhaService
import com.pauta.administracao.outputboundary.service.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ListVotoByEscolhaServiceImpl(

    private val votoService: VotoService

) : ListVotoByEscolhaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(idVoto: Long, votoEscolha: String): Mono<Long> {
        return votoService.getCountVotosByPautaId(idVoto, votoEscolha)
            .doOnSuccess {
                logger.info("Busca completa de votos")
            }
            .onErrorResume {
                logger.error("Error ao deletar o voto message = ${it.message}")
                Mono.error(IllegalAccessException("Erro ao executa a operação de Delete"))
            }
    }
}

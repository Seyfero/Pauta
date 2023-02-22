package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.services.pauta.DeletePautaService
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DeletePautaServiceImpl(

    private val pautaService: PautaService

) : DeletePautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputNome: String): Mono<Boolean> {
        return verifyIfExistsPautaByNome(inputNome)
            .flatMap { exists ->
                if (exists) {
                    pautaService.deleteByName(inputNome).subscribe()
                    logger.info("Order deleted with success!")
                    Mono.just(true)
                } else {
                    logger.error("Order exists!")
                    Mono.error(NoSuchElementException("Order not found!"))
                }
            }
    }

    override fun execute(): Flux<Boolean> {
        return pautaService.removeAll()
    }

    private fun verifyIfExistsPautaById(id: Long): Mono<Boolean> {
        return pautaService.findById(id)
            .hasElement()
    }

    private fun verifyIfExistsPautaByNome(nome: String): Mono<Boolean> {
        return pautaService.findByName(nome)
            .hasElement()
    }
}

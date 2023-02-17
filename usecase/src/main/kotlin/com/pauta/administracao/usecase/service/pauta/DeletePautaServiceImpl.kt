package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.services.pauta.DeletePautaService
import com.pauta.administracao.outputboundary.service.PautaService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeletePautaServiceImpl(

    private val pautaService: PautaService

) : DeletePautaService {

    override fun execute(inputId: Long): Mono<Boolean> {
        return verifyIfExistsPautaById(inputId)
            .flatMap { exists ->
                if (exists) {
                    pautaService.deleteById(inputId)
                    Mono.just(true)
                } else {
                    Mono.error(NoSuchElementException("A pauta não foi encontrada"))
                }
            }
    }

    override fun execute(inputNome: String): Mono<Boolean> {
        return verifyIfExistsPautaByNome(inputNome)
            .flatMap { exists ->
                if (exists) {
                    pautaService.deleteByName(inputNome)
                    Mono.just(true)
                } else {
                    Mono.error(NoSuchElementException("A pauta não foi encontrada"))
                }
            }
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

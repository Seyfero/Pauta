package com.pauta.administracao.input

import com.pauta.administracao.input.services.pauta.DeletePautaService
import com.pauta.administracao.outputboundary.service.PautaService
import reactor.core.publisher.Mono

class DeletePautaImpl(

    private val pautaService: PautaService

) : DeletePautaService {

    override fun execute(inputId: Long): Mono<Boolean> {
        return try {
            verifyIfExistsPautaById(inputId).map {
                pautaService.deleteById(inputId)
                return@map true
            }.switchIfEmpty(Mono.error(Exception("Algo")))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    override fun execute(inputNome: String): Mono<Boolean> {
        return try {
            verifyIfExistsPautaByNome(inputNome).map {
                pautaService.deleteByName(inputNome)
                return@map true
            }.switchIfEmpty(Mono.error(Exception("Algo")))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    private fun verifyIfExistsPautaById(inputId: Long): Mono<Boolean> {
        return pautaService.findById(inputId)
            .hasElement()
            .switchIfEmpty(Mono.empty())
    }

    private fun verifyIfExistsPautaByNome(inputNome: String): Mono<Boolean> {
        return pautaService.findByName(inputNome)
            .hasElement()
            .switchIfEmpty(Mono.empty())
    }
}

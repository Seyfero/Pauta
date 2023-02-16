package com.pauta.administracao.input

import com.pauta.administracao.input.converters.pauta.toInputTotal
import com.pauta.administracao.input.dto.pauta.InputTodasPautasDto
import com.pauta.administracao.input.services.pauta.ListTodasPautasService
import com.pauta.administracao.outputboundary.service.PautaService
import reactor.core.publisher.Flux

class ListTodasPautasImpl(

    private val pautaService: PautaService

) : ListTodasPautasService {

    override fun execute(): Flux<InputTodasPautasDto> {
        return try {
            pautaService.findAll()
                .flatMap {
                    Flux.fromIterable(listOf(it.toInputTotal()))
                }
        } catch (ex: Exception) {
            Flux.error(Exception("algo"))
        }
    }
}

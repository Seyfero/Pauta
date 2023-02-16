package com.pauta.administracao.input

import com.pauta.administracao.input.converters.pauta.toInputAtivos
import com.pauta.administracao.input.dto.pauta.InputPautasAtivasDto
import com.pauta.administracao.input.services.pauta.ListPautasAtivasService
import com.pauta.administracao.outputboundary.service.PautaService
import reactor.core.publisher.Flux
import java.time.LocalDateTime

class ListPautasAtivasImpl(

    private val pautaService: PautaService

) : ListPautasAtivasService {

    override fun execute(): Flux<InputPautasAtivasDto> {
        return try {
            pautaService.findAll()
                .flatMap {
                    if (isValidPautaByDuration(it.toInputAtivos())) {
                        Flux.fromIterable(listOf(it.toInputAtivos()))
                    }
                    Flux.empty()
                }
        } catch (ex: Exception) {
            Flux.error(Exception("algo"))
        }
    }

    private fun isValidPautaByDuration(inputPautasAtivasDto: InputPautasAtivasDto): Boolean {
        if (inputPautasAtivasDto.let { it.pautaDataCriacao.plusSeconds(it.pautaDuracao) }.isBefore(LocalDateTime.now())) {
            return true
        }
        return false
    }
}

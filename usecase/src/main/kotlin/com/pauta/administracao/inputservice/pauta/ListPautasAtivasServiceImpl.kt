package com.pauta.administracao.inputservice.pauta

import com.pauta.administracao.inputservice.converters.pauta.toInputAtivos
import com.pauta.administracao.inputservice.dto.pauta.InputPautasAtivasDto
import com.pauta.administracao.inputservice.services.pauta.ListPautasAtivasService
import com.pauta.administracao.outputboundary.service.PautaService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Service
class ListPautasAtivasServiceImpl(

    private val pautaService: PautaService

) : ListPautasAtivasService {

    override fun execute(): Flux<InputPautasAtivasDto> {
        return pautaService.findAll()
            .filter { isValidPautaByDuration(it.toInputAtivos()) }
            .map { it.toInputAtivos() }
            .onErrorMap { IllegalStateException("Erro ao executar o método execute", it) }
    }

    private fun isValidPautaByDuration(inputPautasAtivasDto: InputPautasAtivasDto): Boolean {
        return inputPautasAtivasDto.pautaDataCriacao.plusSeconds(inputPautasAtivasDto.pautaDuracao)
            .isBefore(LocalDateTime.now())
    }
}

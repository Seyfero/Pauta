package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toInputTotal
import com.pauta.administracao.inputservice.dto.pauta.InputTodasPautasDto
import com.pauta.administracao.inputservice.services.pauta.ListTodasPautasService
import com.pauta.administracao.outputboundary.service.PautaService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ListTodasPautasServiceImpl(

    private val pautaService: PautaService

) : ListTodasPautasService {

    override fun execute(): Flux<InputTodasPautasDto> {
        return pautaService.findAll()
            .map { it.toInputTotal() }
            .onErrorMap { IllegalStateException("Erro ao executar o método execute", it) }
    }
}

package com.pauta.administracao.inputservice.pauta

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

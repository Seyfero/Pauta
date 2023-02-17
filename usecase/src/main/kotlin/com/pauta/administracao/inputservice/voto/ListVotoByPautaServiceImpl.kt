package com.pauta.administracao.inputservice.voto

import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.converters.voto.toInputDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoDto
import com.pauta.administracao.inputservice.services.voto.ListVotoByPautaService
import com.pauta.administracao.outputboundary.service.PautaService
import com.pauta.administracao.outputboundary.service.VotoService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ListVotoByPautaServiceImpl(

    private val votoService: VotoService,
    private val pautaService: PautaService

) : ListVotoByPautaService {
    override fun execute(inputPautaNome: String): Flux<InputVotoDto> {
        return verifyIfExistsPauta(inputPautaNome)
            .flatMapMany { inputVotoDto ->
                inputVotoDto.id?.let { id ->
                    votoService.findByVotoPautaNome(id)
                        .map {
                            it.toInputDto()
                        }
                }
            }.switchIfEmpty(Flux.empty())
    }

    private fun verifyIfExistsPauta(inputPautaNome: String): Mono<InputPautaDto> {
        return pautaService.findByName(inputPautaNome)
            .map {
                it.toInputDto()
            }
            .switchIfEmpty(
                Mono.defer {
                    Mono.error(java.util.NoSuchElementException("Pauta não encontrada"))
                }
            )
    }
}

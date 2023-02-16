package com.pauta.administracao.inputservice.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.services.pauta.UpdatePautaService
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.PautaService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdatePautaServiceImpl(

    private val pautaService: PautaService

) : UpdatePautaService {

    override fun execute(inputPautaDto: InputPautaDto): Mono<InputPautaDto> {
        return try {
            pautaService.findByName(inputPautaDto.pautaNome)
                .flatMap {
                    pautaService.update(inputPautaDto.toDomain().toOutputDto().copy(id = it.id)).map { pauta ->
                        pauta.toInputDto()
                    }
                }
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }
}

package com.pauta.administracao.inputservice.voto

import com.pauta.administracao.inputservice.converters.voto.toDomain
import com.pauta.administracao.inputservice.dto.voto.InputVotoDto
import com.pauta.administracao.inputservice.services.voto.CreateVotoService
import com.pauta.administracao.outputboundary.converters.voto.toOutputDto
import com.pauta.administracao.outputboundary.service.PautaService
import com.pauta.administracao.outputboundary.service.VotoService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3
import java.time.LocalDateTime

@Service
class CreateVotoServiceImpl(

    private val votoService: VotoService,
    private val pautaService: PautaService

) : CreateVotoService {
    override fun execute(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return verifyIfCanPersistVoto(inputVotoDto)
            .flatMap { canPersist ->
                if (canPersist) {
                    votoService.create(inputVotoDto.toDomain().toOutputDto())
                    Mono.just(true)
                } else {
                    Mono.error(IllegalStateException("Não é possível persistir o voto."))
                }
            }
            .onErrorResume { throwable: Throwable ->
                Mono.error(IllegalStateException("Erro ao executar o método execute", throwable))
            }
    }

    private fun verifyIfCanPersistVoto(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return Mono.zip(
            isValidPautaByTime(inputVotoDto),
            verifyUserCanVoteThisPauta(inputVotoDto),
            verifyIfExistsPauta(inputVotoDto)
        )
            .map { tupla ->
                val (fun1, fun2, fun3) = tupla
                fun1 && fun2 && fun3
            }
    }

    private fun isValidPautaByTime(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return Mono.just(
            inputVotoDto.inputVotoPauta.let {
                it.pautaDataCriacao.plusSeconds(it.pautaDuracao).isBefore(LocalDateTime.now())
            }
        )
    }

    private fun verifyUserCanVoteThisPauta(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return votoService.findByVotoPautaAndVotoUsuario(
            inputVotoDto.inputVotoPauta.id,
            inputVotoDto.inputVotoUsuario.id
        )
            .hasElement()
    }

    private fun verifyIfExistsPauta(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return pautaService.findByName(inputVotoDto.inputVotoPauta.pautaNome)
            .hasElement()
    }
}

package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.domain.exception.ExpiredPautaException
import com.pauta.administracao.inputservice.converters.voto.toDomain
import com.pauta.administracao.inputservice.dto.voto.InputVotoDto
import com.pauta.administracao.inputservice.services.voto.CreateVotoService
import com.pauta.administracao.outputboundary.converters.voto.toOutputDto
import com.pauta.administracao.outputboundary.service.PautaService
import com.pauta.administracao.outputboundary.service.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3
import reactor.kotlin.core.util.function.component4
import java.time.LocalDateTime

@Service
class CreateVotoServiceImpl(

    private val votoService: VotoService,
    private val pautaService: PautaService

) : CreateVotoService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return verifyIfCanPersistVoto(inputVotoDto)
            .flatMap { canPersist ->
                if (canPersist) {
                    votoService.create(inputVotoDto.toDomain().toOutputDto())
                    logger.info("Vote created with success!")
                    Mono.just(true)
                } else {
                    Mono.error(IllegalStateException("Error to persist vote!"))
                }
            }
            .onErrorResume { throwable: Throwable ->
                logger.error("Error to validate vote!")
                Mono.error(IllegalStateException("Error to validate vote!", throwable))
            }
    }

    private fun verifyIfCanPersistVoto(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return Mono.zip(
            isValidPautaByTime(inputVotoDto),
            verifyUserCanVoteThisPauta(inputVotoDto),
            verifyIfExistsPauta(inputVotoDto),
            validateVotoValute(inputVotoDto)
        )
            .map { tupla ->
                val (fun1, fun2, fun3, fun4) = tupla
                fun1 && fun2 && fun3 && fun4
            }
    }

    private fun isValidPautaByTime(inputVotoDto: InputVotoDto): Mono<Boolean> {
        return inputVotoDto.inputVotoPauta.let {
            if(it.pautaDataCriacao.plusSeconds(it.pautaDuracao).isBefore(LocalDateTime.now())) {
                logger.info("This order is valid!")
                Mono.just(true)
            } else {
                Mono.error(ExpiredPautaException("Order expired!"))
            }
                .doOnError {
                    logger.error("This order expired!")
                }
        }

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

    private fun validateVotoValute(inputVotoDto: InputVotoDto): Mono<Boolean> {
        val regex = Regex("^(sim|n[aã]o)$")
        return if (regex.matches(inputVotoDto.votoEscolha)) {
            Mono.just(true)
        } else {
            Mono.error(IllegalArgumentException("The value of vote is invalid!"))
        }
            .doOnError {
                logger.error("Error to validate vote message = ${it.message}")
            }
    }
}

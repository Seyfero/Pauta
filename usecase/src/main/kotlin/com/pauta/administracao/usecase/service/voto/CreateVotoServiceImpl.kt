package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.domain.exception.ExpiredPautaException
import com.pauta.administracao.domain.exception.IllegalPautaException
import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.converters.voto.toDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoExternalDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoInternalDto
import com.pauta.administracao.inputservice.services.voto.CreateVotoService
import com.pauta.administracao.outputboundary.converters.voto.toOutputDto
import com.pauta.administracao.outputboundary.service.gateway.ValidateExternalCallUserCpfService
import com.pauta.administracao.outputboundary.service.repository.PautaService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3
import java.time.LocalDateTime

@Service
class CreateVotoServiceImpl(

    private val votoService: VotoService,
    private val pautaService: PautaService,
    private val validateExternalCallUserCpfService: ValidateExternalCallUserCpfService,

) : CreateVotoService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputVotoExternalDto: InputVotoExternalDto): Mono<Boolean> {
        return validUserCpf(inputVotoExternalDto.cpfUsuario)
            .flatMap {
                logger.info("Cpf's user could be validate!")
                verifyIfCanPersistVoto(inputVotoExternalDto.cpfUsuario, inputVotoExternalDto)
            }
            .flatMap {
                logger.info("Vote could be validate!")
                votoService.create(it.toDomain().toOutputDto())
            }
            .onErrorResume { throwable: Throwable ->
                logger.error("Error to persist vote message = ${throwable.message}!!")
                Mono.error(IllegalStateException("Error to persist vote!"))
            }
    }

    private fun validUserCpf(cpf: String): Mono<Boolean> {
        return validateExternalCallUserCpfService.validateExternalCallUserCpf(cpf)
            .flatMap {
                if (it.contains("ABLE_TO_VOTE")) {
                    return@flatMap Mono.just(true)
                }
                Mono.just(false)
            }
            .onErrorResume {
                logger.error("Error to validate cpf on vote by external call!")
                Mono.error(IllegalStateException("Error to validate cpf on vote!"))
            }
    }

    private fun verifyIfCanPersistVoto(userCpf: String, inputVotoExternalDto: InputVotoExternalDto): Mono<InputVotoInternalDto> {
        return verifyIfExistsPauta(inputVotoExternalDto.pautaNome)
            .flatMap { inputPauta ->
                logger.info("Order could be founded!")
                verifyPreConditionsToCreateVote(userCpf, inputVotoExternalDto, inputPauta)
                    .flatMap {
                        if (it) {
                            Mono.just(InputVotoInternalDto(null, inputVotoExternalDto.votoEscolha, userCpf, inputPauta.id))
                        } else {
                            Mono.error(NoSuchElementException("Error to validate user!"))
                        }
                    }
            }
            .switchIfEmpty(Mono.error(NoSuchElementException("")))
            .onErrorResume {
                logger.error("")
                Mono.error(IllegalStateException("Error to validate user!", it))
            }
    }

    private fun verifyPreConditionsToCreateVote(
        userCpf: String,
        inputVotoExternalDto: InputVotoExternalDto,
        inputPautaDto: InputPautaDto
    ): Mono<Boolean> {
        return Mono.zip(
            isValidPautaByTime(inputPautaDto),
            validateVoteValue(inputVotoExternalDto),
            verifyIfExistsVoteWithCpfUser(userCpf, inputPautaDto.id)
        )
            .map { tupla ->
                val (fun1, fun2, fun3) = tupla
                fun1 && fun2 && !fun3
            }
            .switchIfEmpty(
                Mono.error(NoSuchElementException("Order couldn't be founded!"))
            )
            .doOnError {
                logger.error("Order couldn't be founded!")
            }
    }

    private fun verifyIfExistsPauta(nomePuata: String): Mono<InputPautaDto> {
        return pautaService.findByName(nomePuata)
            .map {
                logger.info("Founded order by name!")
                it.toInputDto()
            }
            .switchIfEmpty(Mono.error(IllegalPautaException("This order doesn't exists in database!")))
            .doOnError {
                logger.error("Error to find order!")
            }
    }

    private fun isValidPautaByTime(inputPautaDto: InputPautaDto): Mono<Boolean> {
        return inputPautaDto.let {
            if (it.pautaDataCriacao.plusSeconds(it.pautaDuracao).isAfter(LocalDateTime.now())) {
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

    private fun validateVoteValue(inputVotoExternalDto: InputVotoExternalDto): Mono<Boolean> {
        val regex = Regex("^(sim|n[aã]o)$")
        return if (regex.matches(inputVotoExternalDto.votoEscolha.lowercase())) {
            logger.info("Vote value could be validate!")
            Mono.just(true)
        } else {
            Mono.error(IllegalArgumentException("The value of vote is invalid!"))
        }
            .doOnError {
                logger.error("Error to validate vote message = ${it.message}")
            }
    }

    private fun verifyIfExistsVoteWithCpfUser(userCpf: String, idPauta: Long?): Mono<Boolean> {
        return votoService.findByVotoPautaAndVotoUsuarioCpf(
            idPauta,
            userCpf
        )
            .hasElement()
            .onErrorResume { throwable: Throwable ->
                logger.error("Error to create user message = ${throwable.message}!!")
                Mono.error(IllegalStateException("Error to validate user!", throwable))
            }
    }
}

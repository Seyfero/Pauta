package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.domain.exception.ExpiredPautaException
import com.pauta.administracao.domain.exception.IllegalPautaException
import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoExternalDto
import com.pauta.administracao.inputservice.services.voto.CreateVotoService
import com.pauta.administracao.outputboundary.service.gateway.ValidateUserVoteByCpfService
import com.pauta.administracao.outputboundary.service.repository.PautaService
import com.pauta.administracao.outputboundary.service.repository.UsuarioService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3
import java.time.LocalDateTime

@Service
class CreateVotoServiceImpl(

    private val votoService: VotoService,
    private val pautaService: PautaService,
    private val validateUserVoteByCpfService: ValidateUserVoteByCpfService,
    private val usuarioService: UsuarioService

) : CreateVotoService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(inputVotoExternalDto: InputVotoExternalDto): Mono<Boolean> {
        return validUserCpf(inputVotoExternalDto.cpfUsuario)
            .flatMap {
                logger.info("Cpf's user could be validate!")
                if (it) {
                    verifyIfCanPersistVoto(inputVotoExternalDto)
                    logger.info("Vote could be validate!")
                    Mono.just(true)
                } else {
                    Mono.error(UnsupportedOperationException("Vote couldn't be created!"))
                }
            }
            .switchIfEmpty {
                Mono.error(UnsupportedOperationException("Cpf couldn't be validate!"))
            }
            .doOnError {
                logger.error("Error in operation on create vote!")
            }
    }

    private fun validUserCpf(cpf: String): Mono<Boolean> {
        return validateUserVoteByCpfService.validateExternalCallUserCpf(cpf)
            .flatMap {
                if (it.contains("ABLE_TO_VOTE")) {
                    Mono.just(true)
                }
                Mono.just(false)
            }
            .onErrorResume { throwable: Throwable ->
                logger.error("Error to validate vote!")
                Mono.error(IllegalStateException("Error to validate vote!", throwable))
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

    private fun verifyIfCanPersistVoto(inputVotoExternalDto: InputVotoExternalDto): Mono<Boolean> {
        return verifyIfExistsPauta(inputVotoExternalDto.pautaNome)
            .flatMap {
                logger.info("Order could be founded!")
                Mono.zip(
                    isValidPautaByTime(it),
                    validateVoteValue(inputVotoExternalDto),
                    verifyUserCanVoteThisPauta(inputVotoExternalDto.cpfUsuario, it.id)
                )
                    .map { tupla ->
                        val (fun1, fun2, fun3) = tupla
                        fun1 && fun2 && fun3
                    }
            }
            .switchIfEmpty(
                Mono.error(NoSuchElementException("Order couldn't be founded!"))
            )
            .doOnError {
                logger.error("Order couldn't be founded!")
            }
    }

    private fun isValidPautaByTime(inputPautaDto: InputPautaDto): Mono<Boolean> {
        return inputPautaDto.let {
            if (it.pautaDataCriacao.plusSeconds(it.pautaDuracao).isBefore(LocalDateTime.now())) {
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

    private fun verifyUserCanVoteThisPauta(cpfUsuario: String, idPauta: Long?): Mono<Boolean> {
        return usuarioService.findByCpf(cpfUsuario)
            .flatMap {
                logger.info("User could be founded!")
                votoService.findByVotoPautaAndVotoUsuario(
                    idPauta,
                    it.id
                )
                    .hasElement()
            }
            .switchIfEmpty {
                logger.info("User couldn't be founded!")
                Mono.just(false)
            }
    }
}
